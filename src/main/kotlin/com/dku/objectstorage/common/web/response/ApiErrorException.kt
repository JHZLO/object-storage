package com.dku.objectstorage.common.web.response

import com.dku.objectstorage.common.web.response.ApiError

open class ApiErrorException(
    val error: ApiError,
    cause: Throwable? = null,
) : RuntimeException(cause)
