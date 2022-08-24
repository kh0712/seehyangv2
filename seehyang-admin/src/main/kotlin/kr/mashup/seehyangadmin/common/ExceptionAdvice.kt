package kr.mashup.seehyangadmin.common

import kr.mashup.seehyangadmin.EmptyResponse
import kr.mashup.seehyangadmin.SeehyangAdminResponse
import kr.mashup.seehyangcore.exception.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import javax.naming.ServiceUnavailableException


@RestControllerAdvice
class ExceptionAdvice {

    private val log: Logger = LoggerFactory.getLogger("Seehyang Exception Advice")

    /**
     * MVC LEVEL EXCEPTION
     */
    @ExceptionHandler(HttpMediaTypeException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleHttpMediaTypeException(e: HttpMediaTypeException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleHttpMediaTypeException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.BAD_REQUEST, e.message ?: "")
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleMethodArgumentTypeMismatchException(e: MethodArgumentTypeMismatchException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleMethodArgumentTypeMismatchException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.BAD_REQUEST, e.message ?: "")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleHttpMessageNotReadableException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.BAD_REQUEST, e.message ?: "")
    }

    /**
     * Validation Exception
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleMethodArgumentNotValidException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.BAD_REQUEST, e.bindingResult.allErrors[0].defaultMessage ?: "")
    }


    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleIllegalArgumentException(e: IllegalArgumentException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleIllegalArgumentException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.BAD_REQUEST, e.message ?: "")
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleBadRequestException(e: BadRequestException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleBadRequestException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(e.code, e.message ?: "")
    }

    @ExceptionHandler(UnauthorizedException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleUnauthorizedException(e: UnauthorizedException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleUnauthorizedException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(e.code, e.message)
    }


    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleNotFoundException(e: NotFoundException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleNotFoundException: {}", e.message, e)
        return SeehyangAdminResponse.fail(e.code, e.message)
    }

    @ExceptionHandler(InternalServerException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleInternalServerErrorException(e: InternalServerException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleInternalServerErrorException: {}", e.message, e)
        return SeehyangAdminResponse.fail(e.code, e.message)
    }

    @ExceptionHandler(ServiceUnavailableException::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleServiceUnavailableException(e: ServiceUnavailableException): SeehyangAdminResponse<EmptyResponse> {
        log.info("handleServiceUnavailableException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.INTERNAL_SERVER_ERROR.code, e.message?:"")
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.OK)
    fun handleException(e: Exception): SeehyangAdminResponse<*>? {
        log.error("handleException: {}", e.message ?: "", e)
        return SeehyangAdminResponse.fail(ResultCode.INTERNAL_SERVER_ERROR)
    }

}