package com.dku.objectstorage.storage.dto

import com.dku.objectstorage.storage.domain.entity.FileMetadata
import com.dku.objectstorage.storage.domain.entity.vo.Permission

data class FileMetadataResponse(
    val id: String,
    val originalName: String,
    val size: Long,
    val contentType: String,
    val uploadedAt: String,
    val permission: Permission,
    val downloadUrl: String
) {
    companion object {
        fun from(entity: FileMetadata): FileMetadataResponse {
            return FileMetadataResponse(
                id = entity.id,
                originalName = entity.originalName,
                size = entity.size,
                contentType = entity.contentType,
                uploadedAt = entity.uploadedAt.toString(),
                permission = entity.permission,
                downloadUrl = "/api/v1/files/download/${entity.id}"
            )
        }
    }
}
