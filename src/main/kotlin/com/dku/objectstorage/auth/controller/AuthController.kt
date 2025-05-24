package com.dku.objectstorage.auth.controller

import com.dku.objectstorage.auth.application.AuthFacade
import com.dku.objectstorage.auth.controller.swagger.AuthControllerSpec
import com.dku.objectstorage.auth.dto.LoginRequest
import com.dku.objectstorage.auth.dto.SignUpRequest
import com.dku.objectstorage.common.web.response.ApiResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authFacade: AuthFacade
) : AuthControllerSpec {

    @PostMapping("/signup")
    override fun signup(@RequestBody request: SignUpRequest): ApiResponse<Boolean> {
        authFacade.signUp(request)
        return ApiResponse.success(true)
    }

    @PostMapping("/login")
    override fun login(@RequestBody request: LoginRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.login(request, response)
        return ApiResponse.success(true)
    }

    @PostMapping("/logout")
    override fun logout(request: HttpServletRequest, response: HttpServletResponse): ApiResponse<Boolean> {
        authFacade.logout(request, response)
        return ApiResponse.success(true)
    }
}
