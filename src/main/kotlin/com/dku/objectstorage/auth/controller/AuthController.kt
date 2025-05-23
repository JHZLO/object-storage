package com.dku.objectstorage.auth.controller

import com.dku.objectstorage.auth.application.AuthFacade
import com.dku.objectstorage.auth.controller.swagger.AuthControllerSpec
import com.dku.objectstorage.auth.dto.LoginRequest
import com.dku.objectstorage.auth.dto.SignUpRequest
import com.dku.objectstorage.common.web.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val authFacade: AuthFacade
) : AuthControllerSpec {

    override fun signup(request: SignUpRequest): ApiResponse<Boolean> {
        authFacade.signUp(request)
        return ApiResponse.success(true)
    }

    override fun login(request: LoginRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.login(request, response)
        return ApiResponse.success(true)
    }

    override fun logout(request: HttpServletRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.logout(request, response)
        return ApiResponse.success(true)
    }
}
