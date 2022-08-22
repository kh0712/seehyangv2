package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangbusiness.port.StorageService
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangcore.vo.StorageType
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.image.domain.ImageDomain
import kr.mashup.seehyangrds.user.service.UserQueryDomain
import java.io.InputStream
import java.time.LocalDateTime

@TransactionalService
class ImageService(
    private val userQueryDomain: UserQueryDomain,
    private val imageDomain: ImageDomain,
    private val storageService: StorageService,
) {

    fun saveImage(uploaderId: Long, inputStream: InputStream, extension:String): Long {

        val uploader = userQueryDomain.getActiveByIdOrThrow(uploaderId)
        val storageInfo = storageService.save(inputStream,extension)
        val image = imageDomain.save(uploader, storageInfo.url, storageInfo.storageType)

        return image.id!!
    }

    fun getActiveImage(imageId: Long):ImageInfo{

        val image = imageDomain.getByIdOrThrow(imageId)
        if(image.status != ImageStatus.ACTIVE){
            throw NotFoundException(ResultCode.NOT_FOUND_IMAGE)
        }
        return ImageInfo(UserInfo.from(image.uploader), image.url, image.status, image.type, image.createdAt, image.updatedAt)
    }

}

data class ImageInfo(
    val uploader:UserInfo,
    val url : String,
    val status: ImageStatus,
    val type: StorageType,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)