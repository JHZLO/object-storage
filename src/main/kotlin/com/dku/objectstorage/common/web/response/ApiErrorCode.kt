package com.dku.objectstorage.common.web.response

data class ApiErrorCode(val value: String) {
    companion object {
        fun of(code: String) = ApiErrorCode(code)
    }
}
