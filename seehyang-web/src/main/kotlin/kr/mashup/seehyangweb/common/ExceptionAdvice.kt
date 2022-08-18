package kr.mashup.seehyangweb.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionAdvice {

    private val logger: Logger = LoggerFactory.getLogger("Seehyang Exception Advice")

}