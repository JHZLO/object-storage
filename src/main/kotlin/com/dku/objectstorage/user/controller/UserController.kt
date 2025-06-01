package com.dku.objectstorage.user.controller

import com.dku.objectstorage.common.annotation.loginUser.LoginUser
import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.user.application.UserFacade
import com.dku.objectstorage.user.dto.UserResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userFacade: UserFacade
){
    @GetMapping("/info")
    fun getMyInfo(@LoginUser userId: Long): ApiResponse<UserResponse> {
        val response: UserResponse = userFacade.getUserInfo(userId)
        return ApiResponse.success(response)
    }
}
