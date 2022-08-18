package kr.mashup.seehyangweb.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kr.mashup.seehyangcore.exception.ResultCode
import org.springframework.data.domain.Page

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeehyangResponse<T>(
    val data: T,
    val code: Int,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageResponse: PageResponse?
) {
    companion object {

        fun success(): SeehyangResponse<EmptyResponse> {
            return SeehyangResponse(EmptyResponse(), ResultCode.OK.code, ResultCode.OK.message,null)
        }

        fun <T> success(data: T): SeehyangResponse<T> {
            return SeehyangResponse(data, ResultCode.OK.code, ResultCode.OK.message,null)
        }
        fun <T> success(data: List<T>): SeehyangResponse<List<T>> {
            return SeehyangResponse(data, ResultCode.OK.code, ResultCode.OK.message,null)
        }

        fun <T> success(data: Page<T>): SeehyangResponse<List<T>> {
            return SeehyangResponse(data.content, ResultCode.OK.code, ResultCode.OK.message, PageResponse.from(data))
        }

        fun fail(resultCode: ResultCode): SeehyangResponse<EmptyResponse> {
            return SeehyangResponse(EmptyResponse(), resultCode.code, resultCode.message,null)
        }

        fun fail(resultCode: ResultCode, message: String): SeehyangResponse<EmptyResponse> {
            return SeehyangResponse(EmptyResponse(), resultCode.code, message, null)
        }
    }
}


data class PageResponse private constructor(
    private val number: Int,
    private val size: Int,
    private val totalCount: Int
) {
    companion object {
        fun <T> from(page: Page<T>): PageResponse {
            return PageResponse(
                page.number,
                page.size,
                page.totalElements.toInt()
            )
        }
    }
}

@JsonSerialize
class EmptyResponse()