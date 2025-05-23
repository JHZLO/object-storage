package com.dku.objectstorage.common.infrastructure.jwt

import com.dku.objectstorage.auth.infrastructure.TokenService
import com.dku.objectstorage.auth.service.AuthService
import com.dku.objectstorage.common.configuration.JwtConfig
import com.dku.objectstorage.user.domain.entity.User
import com.dku.objectstorage.user.domain.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

@Component
class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider,
    private val jwtValidator: JwtValidator,
    private val userService: UserService,
    private val tokenService: TokenService,
    private val authService: AuthService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val AT_COOKIE_MAX_AGE = (JwtConfig.ACCESS_TOKEN_EXPIRATION_TIME / 1000).toInt()
        private val RT_COOKIE_MAX_AGE = (JwtConfig.REFRESH_TOKEN_EXPIRATION_TIME / 1000).toInt()
    }

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val accessToken = getCookieValue(request, "access_token")
            val refreshToken = getCookieValue(request, "refresh_token")

            when {
                isValidToken(accessToken) -> applyAuthentication(accessToken!!)
                isValidToken(refreshToken) -> reissueTokensAndAuthenticate(refreshToken!!, response)
            }
        } catch (e: Exception) {
            log.error("JWT 인증 처리 중 오류 발생: ${e.message}", e)
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }

    private fun isValidToken(token: String?): Boolean {
        return token != null && jwtValidator.validateToken(token)
    }

    @Throws(IOException::class)
    private fun reissueTokensAndAuthenticate(refreshToken: String, response: HttpServletResponse) {
        val userId = tokenService.getUserId(refreshToken) ?: run {
            logoutAndProceed(response)
            return
        }

        val rtKey = tokenService.getRTKey(refreshToken)
        if (!tokenService.isValid(rtKey, userId)) {
            invalidateSession(response)
            return
        }

        val user: User = userService.getById(userId.toLong())
        val newAccessToken = issueNewTokens(response, user, rtKey)
        applyAuthentication(newAccessToken)
    }

    private fun logoutAndProceed(response: HttpServletResponse) {
        authService.logout(response)
    }

    @Throws(IOException::class)
    private fun invalidateSession(response: HttpServletResponse) {
        authService.logout(response)
        response.contentType = "application/json;charset=UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.writer.write("""{"code":"INVALID_TOKEN","message":"유효하지 않은 토큰"}""")
    }

    private fun issueNewTokens(response: HttpServletResponse, user: User, oldRtKey: String): String {
        val newAccessToken = jwtProvider.generateAccessToken(user.id, user.userRole)
        val newRefreshToken = jwtProvider.generateRefreshToken()

        val newRtKey = tokenService.getRTKey(newRefreshToken)
        tokenService.saveRT(newRtKey, user.id.toString())
        tokenService.delete(oldRtKey)

        addCookie(response, "access_token", newAccessToken, AT_COOKIE_MAX_AGE)
        addCookie(response, "refresh_token", newRefreshToken, RT_COOKIE_MAX_AGE)

        return newAccessToken
    }

    private fun applyAuthentication(token: String) {
        val userId = jwtValidator.getSubject(token).toLong()
        val role = jwtValidator.getRole(token).name

        val auth = UsernamePasswordAuthenticationToken(
            userId, null, listOf(SimpleGrantedAuthority("ROLE_$role"))
        )
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun addCookie(response: HttpServletResponse, name: String, value: String, maxAgeSeconds: Int) {
        val cookie = Cookie(name, value).apply {
            path = "/"
            isHttpOnly = true
            secure = false // TODO: 개발 환경용
            maxAge = maxAgeSeconds
        }
        response.addCookie(cookie)
    }

    private fun getCookieValue(request: HttpServletRequest, name: String): String? {
        val cookies = request.cookies ?: return null
        return cookies.find { it.name == name }?.value
    }
}
