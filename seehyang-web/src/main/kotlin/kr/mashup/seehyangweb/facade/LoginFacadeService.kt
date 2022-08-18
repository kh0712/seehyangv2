package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.UserService
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.auth.UserAuthService
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.stereotype.Service

@NonTransactionalService
class LoginFacadeService(
    private val userService: UserService,
    private val userAuthService: UserAuthService,
) {

    fun login(request: LoginRequest): LoginResponse {
        val email = request.email
        val password = request.password

        val userInfo = userService.getByEmailAndPasswordOrThrow(email, password)
        val token = userAuthService.getToken(UserAuth(userInfo.id!!))

        return LoginResponse(token)
    }

}

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
)