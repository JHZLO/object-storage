package com.dku.objectstorage.storage.domain.service

import com.dku.objectstorage.common.infrastructure.exception.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
class LocalFileStorageService {
    private val storageDir: Path = Paths.get("storage")

    fun save(file: MultipartFile, storedFileName: String): Path {
        if (!Files.exists(storageDir)) Files.createDirectories(storageDir)
        val targetPath = storageDir.resolve(storedFileName)
        file.inputStream.use { input ->
            Files.copy(input, targetPath)
        }
        return targetPath
    }

    fun load(storedPath: String): Resource {
        val path = Paths.get(storedPath)
        if (!Files.exists(path)) {
            throw BadRequestException("objectstorage.storage.file-not-exist")
        }
        return UrlResource(path.toUri())
    }
}
