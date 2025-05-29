package com.dku.objectstorage.storage.controller.swagger

import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.storage.domain.entity.vo.Permission
import com.dku.objectstorage.storage.dto.FileUploadResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.Resource
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

@Tag(name = "ObjectStorage File Upload/Download", description = "파일 업로드/다운로드 API")
interface StorageControllerSpec {

    @Operation(summary = "파일 업로드", description = "파일을 업로드합니다.")
    fun uploadFile(
        @Parameter(description = "업로드할 파일")
        file: MultipartFile,

        uploaderId: Long,

        @Parameter(description = "파일 접근 권한 (PUBLIC, PRIVATE, SECRET)")
        permission: Permission,

        @Parameter(description = "SECRET 권한일 경우 비밀번호 (선택)", required = false)
        password: String?
    ): ApiResponse<FileUploadResponse>

    @Operation(
        summary = "파일 다운로드(웹뷰 지원)",
        description = "fileId로 파일을 다운로드(혹은 웹뷰에서 바로 볼 수 있도록)합니다.",
    )
    fun downloadFile(
        @Parameter(description = "다운로드할 파일의 ID")
        fileId: String,

        @Parameter(description = "SECRET 권한 파일의 비밀번호", required = false)
        password: String?
    ): ResponseEntity<Resource>
}
