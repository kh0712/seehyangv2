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
import kr.mashup.seehyangrds.user.service.UserCommandDomain
import kr.mashup.seehyangrds.user.service.UserQueryDomain
import kr.mashup.seehyangrds.user.service.UserSaveCommand
import org.hibernate.validator.constraints.Length
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Email
import javax.validation.constraints.Min

@Validated
@TransactionalService
class UserService(
    private val userQueryDomain: UserQueryDomain,
    private val userCommandDomain: UserCommandDomain,
    private val userInfoValidator: UserInfoValidator,
    private val imageService: ImageDomain,
) {

    // 조회

    fun getActiveUserOrThrow(id: Long): UserInfo {
        val user = userQueryDomain.getActiveByIdOrThrow(id)
        return UserInfo.from(user)
    }

    fun getActiveUserOrThrow(email:String, password:String): UserInfo{
        val user = userQueryDomain.getActiveByEmailOrThrow(email)
        if(!user.isMatchedPassword(password) || user.status != UserStatus.ACTIVE){
            throw NotFoundException(ResultCode.NOT_FOUND_USER)
        }
        return UserInfo.from(user)
    }


    fun isDuplicatedNickname(nickname: String): Boolean {
        return userQueryDomain.existByNickname(nickname)
    }

    // 변경
    fun signUp(command: SignUpCommand): UserInfo {
        val nickname = command.nickname
        val email = command.email
        val password = command.password
        val age = command.age
        val gender = command.gender

        val isExistNickname = userQueryDomain.existByNickname(nickname)
        if(isExistNickname){
            throw BadRequestException(ResultCode.ALREADY_EXIST_NICKNAME)
        }

        val isExistEmail = userQueryDomain.existByEmail(email)
        if(isExistEmail){
            throw BadRequestException(ResultCode.INVALID_EMAIL)
        }

        val user = userCommandDomain.save(
            UserSaveCommand(
                nickname = nickname,
                email = email,
                gender = gender,
                password = password,
                age = age
            )
        )
        return UserInfo.from(user)
    }


    fun changeUserDetail(id: Long, nickname: String?, age: Int?, gender: Gender?) {
        val user = userQueryDomain.getActiveByIdOrThrow(id)

        if (age != null) {
            if(userInfoValidator.isValidUserAge(age).not()){
                throw BadRequestException(ResultCode.INVALID_AGE)
            }
            userCommandDomain.changeAge(user, age)
        }
        if (nickname != null) {
            if(userInfoValidator.isValidUserNickname(nickname).not()){
                throw BadRequestException(ResultCode.INVALID_NICKNAME)
            }
            userCommandDomain.changeNickname(user, nickname)
        }
        if (gender != null) {
            userCommandDomain.changeGender(user, gender)
        }
    }



    fun withdraw(id: Long, password: String) {
        val user = userQueryDomain.getActiveByIdOrThrow(id)
        if(!user.isMatchedPassword(password)){
            throw BadRequestException(ResultCode.INVALID_PASSWORD)
        }
        userCommandDomain.changeStatus(user, UserStatus.WITHDRAW)
    }

    fun changeProfile(id: Long, imageId: Long) {

        val user = userQueryDomain.getActiveByIdOrThrow(id)
        val image = imageService.getByIdOrThrow(id)

        if(image.status != ImageStatus.PENDING){
            throw BadRequestException(ResultCode.ALREADY_EXIST_IMAGE)
        }
        if(image.uploader != user){
            throw BadRequestException(ResultCode.INVALID_IMAGE)
        }

        imageService.changeStatus(image, ImageStatus.ACTIVE)
        val oldProfileImageId = user.profile?.id

        if(oldProfileImageId!=null){
            imageService.changeStatus(image, ImageStatus.DELETED)
        }

        userCommandDomain.changeProfile(user, image)
    }

    fun getActiveUsers(userIds: List<Long>): List<UserInfo> {
        return userQueryDomain.getActiveByIds(userIds).map{UserInfo.from (it)}.toList()
    }

    /**
     * Private
     */


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

data class SignUpCommand(
    @Email
    val email: String,
    @Length(min = 8)
    val password: String,
    @Length(min = 3)
    val nickname: String,
    @Min(3)
    val age: Int,
    val gender: Gender
){
    companion object{
        fun of(email: String, password: String, nickname: String, age: Int, gender: Gender): SignUpCommand{
            return SignUpCommand(email, password, nickname, age, gender)
        }
    }
}