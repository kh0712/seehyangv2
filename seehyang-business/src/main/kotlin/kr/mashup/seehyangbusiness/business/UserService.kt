package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.image.domain.ImageDomain
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangrds.user.entity.User
import kr.mashup.seehyangrds.user.entity.UserStatus
import kr.mashup.seehyangrds.user.service.UserCommandService
import kr.mashup.seehyangrds.user.service.UserQueryDomain
import kr.mashup.seehyangrds.user.service.UserSaveCommand

@TransactionalService
class UserService(
    private val userQueryDomain: UserQueryDomain,
    private val userCommandService: UserCommandService,
    private val imageService: ImageDomain,
) {

    fun getByIdOrThrow(id: Long): UserInfo {
        val user = userQueryDomain.getByIdOrThrow(id)
        checkActiveUser(user)
        return UserInfo.from(user)
    }

    fun getByEmailAndPasswordOrThrow(email:String, password:String): UserInfo{
        val user = userQueryDomain.getByEmailOrThrow(email)
        if(!user.isMatchedPassword(password)){
            throw NotFoundException(ResultCode.NOT_FOUND_USER)
        }
        return UserInfo.from(user)
    }


    fun isDuplicatedNickname(nickname: String): Boolean {
        return userQueryDomain.existByNickname(nickname)
    }

    fun changeUserDetail(id: Long, nickname: String?, age: Int?, gender: Gender?) {
        val user = userQueryDomain.getByIdOrThrow(id)
        checkActiveUser(user)
        if (age != null) {
            userCommandService.changeAge(user, age)
        }
        if (nickname != null) {
            userCommandService.changeNickname(user, nickname)
        }
        if (gender != null) {
            userCommandService.changeGender(user, gender)
        }
    }

    fun signUp(email: String, password: String, nickname: String, age: Int, gender: Gender): Long {
        val isExistNickname = userQueryDomain.existByNickname(nickname)
        if(isExistNickname){
            throw BadRequestException(ResultCode.INVALID_NICKNAME)
        }
        val isExistEmail = userQueryDomain.existByEmail(email)
        if(isExistEmail){
            throw BadRequestException(ResultCode.INVALID_EMAIL)
        }

        val user = userCommandService.save(
            UserSaveCommand(
                nickname = nickname,
                email = email,
                gender = gender,
                password = password,
                age = age
            )
        )
        return user.id!!
    }

    fun withdraw(id: Long, password: String) {
        val user = userQueryDomain.getByIdOrThrow(id)
        checkActiveUser(user)
        if(!user.isMatchedPassword(password)){
            throw BadRequestException(ResultCode.INVALID_PASSWORD)
        }
        userCommandService.changeStatus(user, UserStatus.WITHDRAW)
    }

    fun changeProfile(id: Long, imageId: Long) {
        val image = imageService.getByIdOrThrow(id)
        if(image.status != ImageStatus.PENDING){
            throw BadRequestException(ResultCode.ALREADY_EXIST_IMAGE)
        }
        imageService.changeStatus(image, ImageStatus.ACTIVE)
        val user = userQueryDomain.getByIdOrThrow(id)
        val oldProfileImageId = user.profile?.id
        if(oldProfileImageId!=null){
            imageService.changeStatus(image, ImageStatus.DELETED)
        }
        userCommandService.changeProfile(user, image)
    }

    /**
     * Private
     */

    private fun checkActiveUser(user: User) {
        if (user.status != UserStatus.ACTIVE) {
            throw NotFoundException(ResultCode.NOT_FOUND_USER)
        }
    }

}

data class UserInfo(
    val id: Long?,
    val email: String,
    val nickname: String,
    val age: Int?,
    val gender: Gender,
    val profileUrlId: Long?
) {
    companion object {
        fun from(user: User): UserInfo {
            return UserInfo(
                id = user.id,
                email = user.email,
                nickname = user.nickname,
                age = user.age,
                gender = user.gender,
                profileUrlId = user.profile?.id
            )
        }
    }
}