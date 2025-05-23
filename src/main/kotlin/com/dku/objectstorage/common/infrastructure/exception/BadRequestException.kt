package com.dku.objectstorage.common.infrastructure.exception

import com.dku.objectstorage.common.web.response.ErrorCodeResolvingApiErrorException
import com.dku.objectstorage.common.web.response.ExtendedHttpStatus

class BadRequestException(code: String = "bad-request", cause: Throwable? = null) :
    ErrorCodeResolvingApiErrorException(ExtendedHttpStatus.BAD_REQUEST, code, cause)
