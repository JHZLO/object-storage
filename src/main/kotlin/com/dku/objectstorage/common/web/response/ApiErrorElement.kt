package com.dku.objectstorage.common.web.response

data class ApiErrorElement(
    val appId: String,
    val status: ExtendedHttpStatus,
    val code: ApiErrorCode,
    val message: ApiErrorMessage,
    val data: Any? = null,
)
