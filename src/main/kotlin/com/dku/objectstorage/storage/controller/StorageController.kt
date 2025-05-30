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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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

    @GetMapping("/download/{id}")
    override fun downloadFile(
        @PathVariable id: String,
        @RequestParam(required = false) password: String?,
        @LoginUser userId: Long?,
        @RequestParam(defaultValue = "inline") disposition: String // "inline" 또는 "attachment"
    ): ResponseEntity<Resource> {
        val file = storageFacade.getFile(id, password, userId)

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_TYPE, file.contentType)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "$disposition; filename=\"${URLEncoder.encode(file.originalName, StandardCharsets.UTF_8)}\""
            )
            .body(file.resource)
    }
}
