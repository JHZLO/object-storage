package com.dku.objectstorage.auth.application

import com.dku.objectstorage.auth.dto.LoginRequest
import com.dku.objectstorage.auth.dto.SignUpRequest
import com.dku.objectstorage.auth.infrastructure.TokenService
import com.dku.objectstorage.auth.service.AuthService
import com.dku.objectstorage.common.infrastructure.exception.BadRequestException
import com.dku.objectstorage.common.infrastructure.jwt.JwtProvider
import com.dku.objectstorage.user.domain.entity.User
import com.dku.objectstorage.user.domain.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthFacade(
    private val userService: UserService,
    private val jwtProvider: JwtProvider,
    private val tokenService: TokenService,
    private val authService: AuthService,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(request: SignUpRequest) {
        if (userService.existByEmail(request.email)) {
            throw BadRequestException("objectstorage.auth.email-exists")
        }
        val encodedPassword = passwordEncoder.encode(request.password)
        val user = userService.save(User.of(request, encodedPassword))
    }

    @Transactional
    fun login(request: LoginRequest, response: HttpServletResponse) {
        val user = userService.getByEmail(request.email)

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadRequestException("objectstorage.auth.invalid-password")
        }

        val accessToken = jwtProvider.generateAccessToken(user.id, user.userRole)
        val refreshToken = jwtProvider.generateRefreshToken()

        val rtKey = tokenService.getRTKey(refreshToken)
        tokenService.saveRT(rtKey, user.id.toString())

        authService.login(accessToken, refreshToken, response)
    }

    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        request.cookies?.firstOrNull { it.name == "refresh_token" }?.value?.let { refreshToken ->
            val rtKey = tokenService.getRTKey(refreshToken)
            tokenService.delete(rtKey)
        }

        authService.logout(response)
    }
}
