package com.dku.objectstorage.storage.controller.swagger

import com.dku.objectstorage.common.annotation.loginUser.LoginUser
import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.storage.dto.FileMetadataResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable

@Tag(name = "ObjectStorage File", description = "파일 조회/권한설정")
interface FileControllerSpec {
    @Operation(summary = "내가 업로드한 파일 목록 조회")
    fun getMyFiles(
        @LoginUser userId: Long
    ): ApiResponse<List<FileMetadataResponse>>

    @Operation(summary = "파일 상세 조회", description = "파일의 메타데이터(파일명, 크기, 권한 등)를 조회")
    fun getFileDetail(
        @Parameter(description = "파일 ID") @PathVariable id: String,
        @LoginUser userId: Long
    ): ApiResponse<FileMetadataResponse>
}
