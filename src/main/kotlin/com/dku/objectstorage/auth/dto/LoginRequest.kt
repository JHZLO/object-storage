package com.dku.objectstorage.auth.dto

data class LoginRequest(
    val email: String,
    val password: String,
)
