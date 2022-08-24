package kr.mashup.seehyangweb.api

import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@ApiV1
class UserController(
    private val userFacadeService: UserFacadeService
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/user")
    fun getUser(
        @ApiIgnore userAuth: UserAuth,
    ): SeehyangResponse<UserInfoResponse> {
        val user:UserInfoResponse = userFacadeService.getUser(userAuth)
        return SeehyangResponse.success(user)
    }

    @GetMapping("/user/{nickname}")
    fun validDuplicateNickname(
        @PathVariable nickname: String,
    ): SeehyangResponse<DuplicateNicknameResponse> {
        val duplicateNicknameResponse = userFacadeService.isDuplicateNickname(nickname)
        return SeehyangResponse.success(duplicateNicknameResponse)
    }

    @PostMapping("/user")
    fun signUpUser(
        @Valid @RequestBody request: SignUpRequest,
    ): SeehyangResponse<SignUpResponse> {
        val signUpResponse = userFacadeService.signUpUser(request)
        return SeehyangResponse.success(signUpResponse)
    }

    @DeleteMapping("/user")
    fun withdrawUser(
        @ApiIgnore userAuth: UserAuth,
        @Valid @RequestBody withdrawRequest: WithdrawRequest
    ): SeehyangResponse<EmptyResponse> {

        userFacadeService.withdrawUser(userAuth,withdrawRequest)

        return SeehyangResponse.success()
    }

    @PostMapping("/user/profile")
    fun changeUserProfileImage(
        @ApiIgnore userAuth: UserAuth,
        @Valid @RequestBody request: ProfileUpdateRequest
    ): SeehyangResponse<UserInfoResponse> {

        val changedUser = userFacadeService.changeProfileImage(userAuth, request)

        return SeehyangResponse.success(changedUser)
    }

    @PutMapping("/user")
    fun registerUserDetailInfo(
        @ApiIgnore userAuth: UserAuth,
        @Valid @RequestBody request: RegisterUserDetailRequest
    ): SeehyangResponse<UserInfoResponse> {
        val userInfoResponse = userFacadeService.changeUserDetail(userAuth, request)
        return SeehyangResponse.success(userInfoResponse)
    }

}