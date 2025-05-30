package com.dku.objectstorage.storage.domain.service

import com.dku.objectstorage.storage.domain.entity.FileMetadata
import com.dku.objectstorage.storage.domain.repository.FileMetadataRepository
import org.apache.coyote.BadRequestException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FileMetadataService(
    private val fileMetadataRepository: FileMetadataRepository
) {
    @Transactional
    fun save(metadata: FileMetadata): FileMetadata {
        return fileMetadataRepository.save(metadata)
    }

    @Transactional(readOnly = true)
    fun getById(id: String): FileMetadata {
        return findById(id) ?: throw BadRequestException("objectstorage.storage.file-not-found")
    }

    @Transactional(readOnly = true)
    fun findById(id: String): FileMetadata? {
        return fileMetadataRepository.findByIdOrNull(id);
    }

    @Transactional(readOnly = true)
    fun findByUploaderId(uploaderId: Long): List<FileMetadata> {
        return fileMetadataRepository.findAllByUploaderId(uploaderId)
    }

    fun delete(fileId: String) {
        fileMetadataRepository.deleteById(fileId)
    }
}
