package com.dku.objectstorage.user.application

import com.dku.objectstorage.user.domain.entity.User
import com.dku.objectstorage.user.domain.service.UserService
import com.dku.objectstorage.user.dto.UserResponse
import org.springframework.stereotype.Component

@Component
class UserFacade (
    private val userService: UserService
){
    fun getUserInfo(userId: Long): UserResponse {
        val user: User = userService.getById(userId)
        return UserResponse.from(user)
    }
}
