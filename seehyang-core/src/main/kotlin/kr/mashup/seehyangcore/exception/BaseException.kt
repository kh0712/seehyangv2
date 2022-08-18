package kr.mashup.seehyangcore.exception

open class BaseException(
    val code: Int,
    override val message: String
): RuntimeException()