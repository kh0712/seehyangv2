package kr.mashup.seehyangadmin

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kr.mashup.seehyangcore.exception.ResultCode
import org.springframework.data.domain.Page

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SeehyangAdminResponse<T>(
    val data: T,
    val code: Int,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pageResponse: PageResponse?
) {
    companion object {

        fun success(): SeehyangAdminResponse<EmptyResponse> {
            return SeehyangAdminResponse(EmptyResponse(), ResultCode.OK.code, ResultCode.OK.message, null)
        }

        fun <T> success(data: T): SeehyangAdminResponse<T> {
            return SeehyangAdminResponse(data, ResultCode.OK.code, ResultCode.OK.message, null)
        }
        fun <T> success(data: List<T>): SeehyangAdminResponse<List<T>> {
            return SeehyangAdminResponse(data, ResultCode.OK.code, ResultCode.OK.message, null)
        }

        fun <T> success(data: Page<T>): SeehyangAdminResponse<List<T>> {
            return SeehyangAdminResponse(data.content, ResultCode.OK.code, ResultCode.OK.message, PageResponse.from(data))
        }

        fun fail(resultCode: ResultCode): SeehyangAdminResponse<EmptyResponse> {
            return SeehyangAdminResponse(EmptyResponse(), resultCode.code, resultCode.message, null)
        }

        fun fail(resultCode: ResultCode, message: String): SeehyangAdminResponse<EmptyResponse> {
            return SeehyangAdminResponse(EmptyResponse(), resultCode.code, message, null)
        }
        fun fail(code:Int, message: String): SeehyangAdminResponse<EmptyResponse> {
            return SeehyangAdminResponse(EmptyResponse(), code, message, null)
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