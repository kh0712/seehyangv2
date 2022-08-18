package kr.mashup.seehyangweb.config.resolver

import kr.mashup.seehyangbusiness.business.CommentSortBy
import kr.mashup.seehyangbusiness.business.CommentSortRequest
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
class CommentSortRequestArgumentResolver: HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        if(parameter.parameterType == CommentSortRequest::class.java) return true
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

            val sortBy = if(sortByParam!= null) CommentSortBy.valueOf(sortByParam) else null
            val direction = if(directionParam!=null) Sort.Direction.valueOf(directionParam) else null

            return CommentSortRequest(page = page,size=size, sortBy, direction)

        }catch (e: Exception){
            // logging
            throw BadRequestException(ResultCode.INVALID_PAGING_PARAMETER)
        }

    }
}