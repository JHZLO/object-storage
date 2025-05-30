package com.dku.objectstorage.storage.controller

import com.dku.objectstorage.common.annotation.loginUser.LoginUser
import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.storage.application.StorageFacade
import com.dku.objectstorage.storage.controller.swagger.StorageControllerSpec
import com.dku.objectstorage.storage.domain.entity.vo.Permission
import com.dku.objectstorage.storage.dto.FileUploadResponse
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1")
class StorageController(
    private val storageFacade: StorageFacade
) : StorageControllerSpec {


    @PostMapping("/upload", consumes = ["multipart/form-data"])
    override fun uploadFile(
        @RequestPart("file") file: MultipartFile,
        @LoginUser uploaderId: Long,
        @RequestParam("permission", defaultValue = "PUBLIC") permission: Permission,
        @RequestParam("password", required = false) password: String?
    ): ApiResponse<FileUploadResponse> {
        val response = storageFacade.uploadFile(file, uploaderId, permission, password)
        return ApiResponse.success(response)
    }

    @GetMapping("/download/{fileId}")
    override fun downloadFile(
        @PathVariable fileId: String,
        @RequestParam("password", required = false) password: String?,
        @LoginUser userId: Long?
    ): ResponseEntity<Resource> {
        val fileResponse = storageFacade.getFile(fileId, password, userId)
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(fileResponse.contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"${fileResponse.originalName}\"")
            .body(fileResponse.resource)
    }
}
