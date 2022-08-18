package kr.mashup.seehyangweb.api

import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.LoginFacadeService
import kr.mashup.seehyangweb.facade.LoginRequest
import kr.mashup.seehyangweb.facade.LoginResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@ApiV1
class LoginController(
    private val loginFacadeService: LoginFacadeService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
    ): SeehyangResponse<LoginResponse> {

        val loginResponse = loginFacadeService.login(request)
        return SeehyangResponse.success(loginResponse)
    }
}