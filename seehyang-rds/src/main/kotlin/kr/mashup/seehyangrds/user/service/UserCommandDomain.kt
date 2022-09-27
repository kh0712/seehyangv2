package kr.mashup.seehyangrds.user.service

import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.image.entity.Image
import kr.mashup.seehyangrds.user.entity.User
import kr.mashup.seehyangrds.user.entity.UserStatus
import kr.mashup.seehyangrds.user.repo.UserRepository
import org.springframework.util.StringUtils
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Validated
@TransactionalService
class UserCommandDomain(
    private val userRepository: UserRepository
) {

    fun save(@Valid command: UserSaveCommand): User{
        val existEmail = userRepository.existsByEmail(command.email)
        if(existEmail){
            throw BadRequestException(ResultCode.ALREADY_EXIST_USER)
        }
        return userRepository.save(
            User(
                nickname = command.nickname,
                email = command.email,
                gender = command.gender,
                password = command.password,
                age = command.age
            )
        )
    }

    fun changePassword(user: User, password: String) {
        if (!StringUtils.hasText(password)) {
            throw INVALID_PASSWORD_EXCEPTION
        }
        user.password = password
    }

    fun changeStatus(user: User, status: UserStatus) {
        user.status = status
    }

    fun changeNickname(user: User, nickname: String) {
        if (!StringUtils.hasText(nickname)) {
            throw INVALID_NICKNAME_EXCEPTION
        }
        user.nickname = nickname
    }

    fun changeAge(user: User, age: Int) {
        user.age = age
    }

    fun changeGender(user: User, gender: Gender) {
        user.gender = gender
    }

    fun changeProfile(user: User, image: Image) {
        user.profile = image
    }

    companion object {
        val INVALID_PASSWORD_EXCEPTION = BadRequestException(ResultCode.INVALID_PASSWORD)
        val INVALID_NICKNAME_EXCEPTION = BadRequestException(ResultCode.INVALID_NICKNAME)
    }

}

data class UserSaveCommand(
    @NotBlank
    val nickname: String,
    @Email
    val email: String,
    val gender: Gender,
    @NotBlank
    val password: String,
    val age: Int?
)