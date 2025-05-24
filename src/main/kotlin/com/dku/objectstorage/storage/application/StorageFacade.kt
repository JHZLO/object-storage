package com.dku.objectstorage.storage.application

import com.dku.objectstorage.storage.domain.entity.FileMetadata
import com.dku.objectstorage.storage.domain.entity.vo.Permission
import com.dku.objectstorage.storage.domain.service.FileMetadataService
import com.dku.objectstorage.storage.domain.service.LocalFileStorageService
import com.dku.objectstorage.storage.dto.FileUploadResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class StorageFacade(
    private val localFileStorageService: LocalFileStorageService,
    private val fileMetadataService: FileMetadataService
) {

    fun uploadFile(
        file: MultipartFile,
        uploaderId: Long,
        permission: Permission,
        password: String?
    ): FileUploadResponse {
        // 1. UUID 및 파일명 생성
        val fileId = UUID.randomUUID().toString()
        val extension = file.originalFilename?.substringAfterLast('.', "") ?: "dat"
        val storedFileName = "$fileId.$extension"

        // 2. 로컬 저장소에 파일 저장
        val targetPath = localFileStorageService.save(file, storedFileName)

        // 3. 메타데이터 생성 및 저장
        val metadata = FileMetadata.of(
            id = fileId,
            file = file,
            storedPath = targetPath.toString(),
            uploaderId = uploaderId,
            permission = permission,
            password = password
        )
        fileMetadataService.save(metadata)

        // 4. 응답 반환
        return FileUploadResponse(
            fileId = fileId,
            downloadUrl = "/download/$fileId"
        )
    }
}
