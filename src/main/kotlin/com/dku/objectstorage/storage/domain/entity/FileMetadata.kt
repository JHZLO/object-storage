package com.dku.objectstorage.storage.domain.entity

import com.dku.objectstorage.storage.domain.entity.vo.Permission
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Entity
@Table(name = "file_metadata")
data class FileMetadata(
    @Id
    val id: String,

    @Column(nullable = false)
    val originalName: String,

    @Column(nullable = false)
    val storedPath: String,

    @Column(nullable = false)
    val size: Long,

    @Column(nullable = false)
    val contentType: String,

    @Column(nullable = false)
    val uploadedAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val permission: Permission,

    @Column(nullable = false)
    val uploaderId: Long,

    @Column(nullable = true)
    val downloadPassword: String? = null
){
    companion object {
        fun of(
            id: String,
            file: MultipartFile,
            storedPath: String,
            uploaderId: Long,
            permission: Permission,
            password: String?
        ): FileMetadata {
            return FileMetadata(
                id = id,
                originalName = file.originalFilename ?: "unknown",
                storedPath = storedPath,
                size = file.size,
                contentType = file.contentType ?: "application/octet-stream",
                uploadedAt = LocalDateTime.now(),
                permission = permission,
                uploaderId = uploaderId,
                downloadPassword = if (permission == Permission.SECRET) password else null
            )
        }
    }

}
