package com.dku.objectstorage.auth.dto

data class SignUpRequest(
    val email: String,
    val userName: String,
    val password: String,
)
