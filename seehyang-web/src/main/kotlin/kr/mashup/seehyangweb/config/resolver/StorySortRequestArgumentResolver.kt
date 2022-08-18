package kr.mashup.seehyangweb.config.resolver

import kr.mashup.seehyangbusiness.business.StorySortBy
import kr.mashup.seehyangbusiness.business.StorySortRequest
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.ResultCode
import org.springframework.core.MethodParameter
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class StorySortRequestArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        if(parameter.parameterType == StorySortRequest::class.java) return true
        return false
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        try{
            val page = webRequest.getParameter("page")?.toInt()
            val size = webRequest.getParameter("size")?.toInt()

            val sortByParam = webRequest.getParameter("sortBy")
            val directionParam = webRequest.getParameter("direction")

            val sortBy = if(sortByParam!= null) StorySortBy.valueOf(sortByParam) else null
            val direction = if(directionParam!=null) Sort.Direction.valueOf(directionParam) else null

            return StorySortRequest(page = page,size=size, sortBy, direction)

        }catch (e: Exception){
            // logging
            throw BadRequestException(ResultCode.INVALID_PAGING_PARAMETER)
        }

    }
}