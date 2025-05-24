package com.dku.objectstorage.storage.dto

data class FileUploadResponse(
    val fileId: String,
    val downloadUrl: String
)
