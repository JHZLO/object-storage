package com.dku.objectstorage.common.infrastructure.jwt

import com.dku.objectstorage.common.infrastructure.exception.BadRequestException
import com.dku.objectstorage.user.domain.entity.vo.UserRole
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JwtValidator(
    private val secretKeyFactory: SecretKeyFactory
) {

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token)
            true
        } catch (e: JwtException) {
            false
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getSubject(token: String): String {
        return try {
            Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token).payload.subject
        } catch (e: Exception) {
            throw BadRequestException("objectstorage.auth.invalid-token")
        }
    }

    fun getRole(token: String): UserRole {
        return try {
            val claims: Claims = Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
                .parseSignedClaims(token).payload
            val roleRaw = claims["role", String::class.java]
            UserRole.valueOf(roleRaw)
        } catch (e: Exception) {
            throw BadRequestException("objectstorage.auth.invalid-token")
        }
    }

    fun getExpiration(token: String): Instant {
        return Jwts.parser().verifyWith(secretKeyFactory.createSecretKey()).build()
            .parseSignedClaims(token).payload.expiration.toInstant()
    }
}
