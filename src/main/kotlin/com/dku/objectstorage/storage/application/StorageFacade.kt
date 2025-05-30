package com.dku.objectstorage.storage.application

import com.dku.objectstorage.common.infrastructure.exception.BadRequestException
import com.dku.objectstorage.storage.domain.entity.FileMetadata
import com.dku.objectstorage.storage.domain.entity.vo.Permission
import com.dku.objectstorage.storage.domain.service.FileMetadataService
import com.dku.objectstorage.storage.domain.service.LocalFileStorageService
import com.dku.objectstorage.storage.dto.FileDownloadResponse
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
        val extension = file.originalFilename?.substringAfterLast('.', "") ?: DEFAULT_EXTENSION
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
            downloadUrl = "$DOWNLOAD_URL_PREFIX$fileId"
        )
    }

    fun getFile(fileId: String, password: String?, requesterId: Long?): FileDownloadResponse {
        // 1. 메타데이터 조회
        val metadata = fileMetadataService.findById(fileId)
            ?: throw BadRequestException("objectstorage.storage.file-not-found")

        // 2. 권한 확인
        when (metadata.permission) {
            Permission.PUBLIC -> {
                // 누구나 접근 가능 → 통과
            }

            Permission.PRIVATE -> {
                // 로그인 사용자의 ID와 uploaderId 비교
                if (requesterId == null || metadata.uploaderId != requesterId) {
                    throw BadRequestException("objectstorage.storage.file-not-permission")
                }
            }

            Permission.SECRET -> {
                if (password != metadata.downloadPassword) {
                    throw BadRequestException("objectstorage.storage.password-invalid")
                }
            }
        }

        // 3. 파일 읽기
        val resource = localFileStorageService.load(metadata.storedPath)

        return FileDownloadResponse(
            resource = resource,
            contentType = metadata.contentType,
            originalName = metadata.originalName
        )
    }

    companion object {
        private const val DEFAULT_EXTENSION = "dat"
        private const val DOWNLOAD_URL_PREFIX = "/download/"
    }
}
