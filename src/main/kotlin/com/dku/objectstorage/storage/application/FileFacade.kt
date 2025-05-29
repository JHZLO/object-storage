package com.dku.objectstorage.storage.application

import com.dku.objectstorage.storage.domain.service.FileMetadataService
import com.dku.objectstorage.storage.dto.FileMetadataResponse
import org.springframework.stereotype.Component


@Component
class FileFacade (
    private val fileMetadataService: FileMetadataService
){
    fun getFilesByUser(userId: Long): List<FileMetadataResponse> {
        val files = fileMetadataService.findByUploaderId(userId)
        return files.map { FileMetadataResponse.from(it) }
    }

    fun getFileDetail(id: String, userId: Long): FileMetadataResponse {
        val file = fileMetadataService.getById(id)
        if (file.uploaderId != userId) {
            throw IllegalAccessException("objectstorage.storage.file-not-permission")
        }
        return FileMetadataResponse.from(file)
    }
}
