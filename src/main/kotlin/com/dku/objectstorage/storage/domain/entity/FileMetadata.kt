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
    var originalName: String,

    @Column(nullable = false)
    var storedPath: String,

    @Column(nullable = false)
    var size: Long,

    @Column(nullable = false)
    var contentType: String,

    @Column(nullable = false)
    var uploadedAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var permission: Permission,

    @Column(nullable = false)
    var uploaderId: Long,

    @Column(nullable = true)
    var downloadPassword: String? = null
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

    fun changePermission(newPermission: Permission, password: String?) {
        this.permission = newPermission
        this.downloadPassword = if (newPermission == Permission.SECRET) password else null
    }
}
