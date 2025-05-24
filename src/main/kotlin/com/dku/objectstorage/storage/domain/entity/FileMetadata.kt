package com.dku.objectstorage.storage.domain.entity

import com.dku.objectstorage.storage.domain.entity.vo.Permission
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
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
)
