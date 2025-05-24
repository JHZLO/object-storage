package com.dku.objectstorage.storage.controller.swagger

import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.storage.domain.entity.vo.Permission
import com.dku.objectstorage.storage.dto.FileUploadResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "ObjectStorage File", description = "파일 업로드/다운로드 API")
@RequestMapping("/api/v1/files")
interface StorageControllerSpec {

    @Operation(summary = "파일 업로드", description = "파일을 업로드합니다.")
    @PostMapping("/upload", consumes = ["multipart/form-data"])
    fun uploadFile(
        @Parameter(description = "업로드할 파일")
        file: MultipartFile,

        uploaderId: Long,

        @Parameter(description = "파일 접근 권한 (PUBLIC, PRIVATE, SECRET)")
        permission: Permission,

        @Parameter(description = "SECRET 권한일 경우 비밀번호 (선택)", required = false)
        password: String?
    ): ApiResponse<FileUploadResponse>
}
