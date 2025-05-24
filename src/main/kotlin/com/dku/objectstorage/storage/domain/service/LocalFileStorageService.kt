package com.dku.objectstorage.storage.domain.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
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
}
