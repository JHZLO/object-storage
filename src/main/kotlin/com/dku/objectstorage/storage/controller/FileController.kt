package com.dku.objectstorage.storage.controller

import com.dku.objectstorage.common.annotation.loginUser.LoginUser
import com.dku.objectstorage.common.web.response.ApiResponse
import com.dku.objectstorage.storage.application.FileFacade
import com.dku.objectstorage.storage.controller.swagger.FileControllerSpec
import com.dku.objectstorage.storage.dto.FileMetadataResponse
import com.dku.objectstorage.storage.dto.PermissionChangeRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/files")
class FileController(
    private val fileFacade: FileFacade
) : FileControllerSpec {

    @GetMapping
    override fun getMyFiles(@LoginUser userId: Long): ApiResponse<List<FileMetadataResponse>> {
        val result = fileFacade.getFilesByUser(userId)
        return ApiResponse.success(result)
    }

    @GetMapping("/{id}")
    override fun getFileDetail(@PathVariable id: String, @LoginUser userId: Long): ApiResponse<FileMetadataResponse> {
        val result = fileFacade.getFileDetail(id, userId)
        return ApiResponse.success(result)
    }


    @PutMapping("/{id}/permission")
    override fun changePermission(
        @PathVariable id: String,
        @RequestBody request: PermissionChangeRequest,
        @LoginUser userId: Long
    ): ApiResponse<Boolean> {
        fileFacade.changePermission(id, userId, request)
        return ApiResponse.success(true)
    }

    @DeleteMapping("/{id}")
    override fun deleteFile(@PathVariable id: String, @LoginUser userId: Long): ApiResponse<Boolean> {
        fileFacade.deleteFile(id, userId)
        return ApiResponse.success(true)
    }
}
