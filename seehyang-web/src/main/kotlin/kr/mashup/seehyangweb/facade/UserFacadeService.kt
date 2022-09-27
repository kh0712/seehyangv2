package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.SignUpCommand
import kr.mashup.seehyangbusiness.business.UserInfo
import kr.mashup.seehyangbusiness.business.UserService
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.NonTransactionalService
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@NonTransactionalService
class UserFacadeService(
    private val userService: UserService,
) {

    fun getUser(userAuth: UserAuth): UserInfoResponse {
        val userInfo: UserInfo = userService.getActiveUserOrThrow(id = userAuth.id)
        return UserInfoResponse.from(userInfo)
    }

    fun isDuplicateNickname(nickname: String): DuplicateNicknameResponse {
        val isDuplicated: Boolean = userService.isDuplicatedNickname(nickname)
        return DuplicateNicknameResponse(isDuplicated)
    }

    fun changeUserDetail(userAuth: UserAuth, registerUserDetailRequest: RegisterUserDetailRequest): UserInfoResponse {
        val (nickname, age, gender) = registerUserDetailRequest.copy()
        userService.changeUserDetail(id = userAuth.id, age = age, nickname = nickname, gender = gender)

        val userInfo: UserInfo = userService.getActiveUserOrThrow(id = userAuth.id)

        return UserInfoResponse.from(userInfo)
    }

    fun signUpUser(signUpRequest: SignUpRequest): SignUpResponse {

        val (email, password, nickname, age, gender) = signUpRequest.copy()

        val userInfo: UserInfo =
            userService.signUp(
                SignUpCommand.of(
                    email = email,
                    password = password,
                    nickname = nickname,
                    age = age,
                    gender = gender
                )
            )

        return SignUpResponse(userInfo.id!!)
    }

    fun withdrawUser(userAuth: UserAuth, withdrawRequest: WithdrawRequest) {
        userService.withdraw(userAuth.id, withdrawRequest.password)
    }

    fun changeProfileImage(userAuth: UserAuth, profileUpdateRequest: ProfileUpdateRequest): UserInfoResponse {
        userService.changeProfile(userAuth.id, profileUpdateRequest.imageId)
        val userInfo: UserInfo = userService.getActiveUserOrThrow(id = userAuth.id)
        return UserInfoResponse.from(userInfo)
    }
}

data class RegisterUserDetailRequest(
    val nickname: String?,
    val age: Int?,
    val gender: Gender?
)

data class ProfileUpdateRequest(
    val imageId: Long
)

data class SignUpRequest(
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    val email: String,
    @NotBlank
    val password: String,
    @NotBlank
    val nickname: String,
    @Min(1)
    val age: Int,
    val gender: Gender,
)

data class UserInfoResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val age: Int,
    val gender: Gender,
    val profileImageId: Long?
) {
    companion object {
        fun from(userInfo: UserInfo): UserInfoResponse {
            val (id, email, nickname, age, gender, profileImageId) = userInfo.copy()
            return UserInfoResponse(id!!, email, nickname, age ?: 0, gender, profileImageId)
        }
    }
}

data class DuplicateNicknameResponse(
    val isDuplicated: Boolean
)

data class SignUpResponse(
    val id: Long
)

data class WithdrawRequest(
    @NotBlank
    val password: String
)

