package kr.mashup.seehyangweb.config.resolver

import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.exception.UnauthorizedException
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.auth.UserAuthService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserArgumentResolver(
    private val userAuthService: UserAuthService
): HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        if(parameter.parameterType == UserAuth::class.java) return true
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {

        val bearerToken = webRequest.getHeader("Authorization")?: throw BadRequestException(ResultCode.UNAUTHORIZED_USER)
        if(!bearerToken.startsWith("Bearer ")){
            throw UnauthorizedException(ResultCode.UNAUTHORIZED)
        }
        val token = bearerToken.substring(7)
        val id = userAuthService.getId(token)

        return UserAuth(id)
    }
}