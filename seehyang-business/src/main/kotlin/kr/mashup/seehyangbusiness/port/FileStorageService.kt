package kr.mashup.seehyangbusiness.port

import kr.mashup.seehyangcore.vo.StorageType
import org.springframework.stereotype.Component
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Component
class FileStorageService : StorageService{

    private val fileStoragePath: Path = Paths.get("/Users/user/Desktop/upload").toAbsolutePath().normalize()

    override fun save(inputStream: InputStream, extension:String): StorageInfo {
        val fileNamePrefix = UUID.randomUUID().toString()
        val fileName = "${fileNamePrefix}${extension}"
        val location = fileStoragePath.resolve(fileName)
        Files.copy(inputStream, location)

        return StorageInfo(location.toString(), StorageType.LOCAL)
    }

    init{
        Files.createDirectories(fileStoragePath);
    }
}