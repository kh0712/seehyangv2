package kr.mashup.seehyangrds.image.domain

import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.image.entity.Image
import kr.mashup.seehyangrds.image.repository.ImageRepository
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangcore.vo.StorageType
import kr.mashup.seehyangrds.user.entity.User

@TransactionalService
class ImageDomain(
    private val imageRepository: ImageRepository
) {

    fun save(uploader: User, url:String, type: StorageType): Image {
        if(imageRepository.existsByUrl(url)){
            throw BadRequestException(ResultCode.ALREADY_EXIST_IMAGE)
        }
        val image = Image(uploader = uploader,url = url, status = ImageStatus.PENDING, type = type)
        return imageRepository.save(image)
    }

    fun changeStatus(image: Image, status: ImageStatus){
        image.status = status
    }

    fun getByIdOrThrow(id:Long): Image {
        return imageRepository.findById(id).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_IMAGE) }
    }

    fun getByUrl(url:String): Image {
        return imageRepository.findByUrl(url)?: throw NotFoundException(ResultCode.NOT_FOUND_IMAGE)
    }
}