package com.dku.objectstorage.storage.domain.repository

import com.dku.objectstorage.storage.domain.entity.FileMetadata
import org.springframework.data.jpa.repository.JpaRepository

interface FileMetadataRepository : JpaRepository<FileMetadata, String> {
    fun findAllByUploaderId(uploaderId: Long): List<FileMetadata>
}
