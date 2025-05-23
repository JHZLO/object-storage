package com.dku.objectstorage.auth.infrastructure

import com.dku.objectstorage.common.configuration.JwtConfig
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class TokenService(
    private val redisTemplate: StringRedisTemplate
) {
    fun saveRT(key: String, userId: String) {
        val duration = Duration.ofMillis(JwtConfig.REFRESH_TOKEN_EXPIRATION_TIME)
        redisTemplate.opsForValue().set(key, userId, duration)
    }

    fun isValid(key: String, userId: String): Boolean {
        val stored = redisTemplate.opsForValue().get(key)
        return userId == stored
    }

    fun delete(key: String) {
        redisTemplate.delete(key)
    }

    fun getUserId(refreshToken: String): String? {
        val key = getRTKey(refreshToken)
        return redisTemplate.opsForValue().get(key)
    }

    fun getRTKey(token: String): String {
        return "$RT_PREFIX$token"
    }

    companion object {
        private const val RT_PREFIX = "RT:"
    }
}
