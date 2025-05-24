package com.dku.objectstorage.storage.domain.service

import com.dku.objectstorage.storage.domain.entity.FileMetadata
import com.dku.objectstorage.storage.domain.repository.FileMetadataRepository
import org.springframework.stereotype.Service

@Service
class FileMetadataService(
    private val fileMetadataRepository: FileMetadataRepository
) {
    fun save(metadata: FileMetadata): FileMetadata {
        return fileMetadataRepository.save(metadata)
    }
}
