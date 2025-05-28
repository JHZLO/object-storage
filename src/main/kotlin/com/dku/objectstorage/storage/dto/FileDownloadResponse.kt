package com.dku.objectstorage.storage.dto

import org.springframework.core.io.Resource

data class FileDownloadResponse(
    val resource: Resource,
    val contentType: String,
    val originalName: String
)
