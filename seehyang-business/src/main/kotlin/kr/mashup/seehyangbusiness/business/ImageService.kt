package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangbusiness.port.StorageService
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangcore.vo.StorageType
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.image.domain.ImageDomain
import kr.mashup.seehyangrds.user.entity.User
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

        val uploader = userQueryDomain.getByIdOrThrow(uploaderId)
        val storageInfo = storageService.save(inputStream,extension)
        val image = imageDomain.save(uploader, storageInfo.url, storageInfo.storageType)

        return image.id!!
    }

    fun getImage(imageId: Long):ImageInfo{

        val image = imageDomain.getByIdOrThrow(imageId)
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