package com.dku.objectstorage.common.infrastructure.jwt

import com.dku.objectstorage.common.configuration.JwtConfig
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import java.util.*

@Component
class SecretKeyFactory(
    private val jwtConfig: JwtConfig
) {

    fun createSecretKey(): SecretKey {
        val encodedKey = Base64.getEncoder().encodeToString(jwtConfig.jwtSecret.toByteArray())
        return Keys.hmacShaKeyFor(encodedKey.toByteArray())
    }
}
