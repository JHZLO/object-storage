package com.dku.objectstorage.auth.controller.swagger

import com.dku.objectstorage.auth.dto.LoginRequest
import com.dku.objectstorage.auth.dto.SignUpRequest
import com.dku.objectstorage.common.web.response.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "ObjectStorage AUTH", description = "회원가입/로그인")
@RequestMapping("/api/v1/auth")
interface AuthControllerSpec {

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    fun signup(
        @Parameter(description = "회원가입 정보")
        @RequestBody request: SignUpRequest
    ): ApiResponse<Boolean>

    @Operation(summary = "로그인", description = "로그인")
    @PostMapping("/login")
    fun login(
        @Parameter(description = "로그인 정보")
        @RequestBody request: LoginRequest,
        response: HttpServletResponse
    ): ApiResponse<Boolean>

    @Operation(summary = "로그아웃", description = "JWT 리프레시 토큰 삭제 및 쿠키 초기화")
    @PostMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ApiResponse<Boolean>
}
