package com.dku.objectstorage.user.dto

import com.dku.objectstorage.user.domain.entity.User

data class UserResponse(
    val email: String,
    val userName: String,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                email = user.email,
                userName = user.name
            )
        }
    }
}
