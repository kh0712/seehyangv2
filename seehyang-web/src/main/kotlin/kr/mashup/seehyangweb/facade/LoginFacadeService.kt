package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.UserService
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.auth.UserAuthService
import kr.mashup.seehyangweb.common.NonTransactionalService
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@NonTransactionalService
class LoginFacadeService(
    private val userService: UserService,
    private val userAuthService: UserAuthService,
) {

    fun login(request: LoginRequest): LoginResponse {
        val email = request.email
        val password = request.password

        val userInfo = userService.getActiveUserByEmailAndPasswordOrThrow(email, password)
        val token = userAuthService.getToken(UserAuth(userInfo.id!!))

        return LoginResponse(token)
    }

}

data class LoginRequest(
    @Email(message = "올바르지 않은 이메일 형식입니다.")
    val email: String,
    @NotBlank(message = "비밀번호는 비어있을 수 없습니다.")
    val password: String
)

data class LoginResponse(
    val token: String,
)