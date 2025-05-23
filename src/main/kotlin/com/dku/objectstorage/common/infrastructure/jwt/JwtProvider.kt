package com.dku.objectstorage.common.infrastructure.jwt

import com.dku.objectstorage.common.configuration.JwtConfig
import com.dku.objectstorage.user.domain.entity.vo.UserRole
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*


@Component
class JwtProvider(
    private val secretKeyFactory: SecretKeyFactory
) {

    fun generateAccessToken(userId: Long, role: UserRole): String {
        val now = Instant.now()
        val expiry = now.plusMillis(JwtConfig.ACCESS_TOKEN_EXPIRATION_TIME)

        return Jwts.builder()
            .subject(userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .claim("role", role.name)
            .signWith(secretKeyFactory.createSecretKey())
            .compact()
    }

    fun generateRefreshToken(): String {
        return UUID.randomUUID().toString()
    }
}
