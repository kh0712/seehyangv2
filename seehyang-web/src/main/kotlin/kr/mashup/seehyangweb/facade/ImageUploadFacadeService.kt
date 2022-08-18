package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.ImageService
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.vo.StorageType
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

@NonTransactionalService
class ImageUploadFacadeService(
    private val imageService: ImageService
) {

    fun upload(userAuth: UserAuth, multipartFile: MultipartFile): ImageUploadResponse {
        val contentType = multipartFile.contentType?:throw BadRequestException(ResultCode.INVALID_IMAGE)
        if(!contentType.startsWith("image")){
            throw BadRequestException(ResultCode.INVALID_IMAGE)
        }
        val fileName = multipartFile.originalFilename?:throw BadRequestException(ResultCode.INVALID_IMAGE)
        val extension = fileName.substring(fileName.lastIndexOf("."))
        val imageId:Long = imageService.saveImage(userAuth.id, multipartFile.inputStream,extension )

        return ImageUploadResponse(imageId)
    }

    fun download(imageId:Long): ImageDownloadResponse{
        val imageInfo = imageService.getImage(imageId)
        return if(imageInfo.type== StorageType.LOCAL){
            val readAllBytes = Files.readAllBytes(Path.of(imageInfo.url))
            ImageDownloadResponse(readAllBytes)
        }else{
            // TODO
            ImageDownloadResponse(ByteArray(0))
        }
    }
}

data class ImageDownloadResponse(
    val image: ByteArray
)

data class ImageUploadResponse(
    val imageId:Long
)