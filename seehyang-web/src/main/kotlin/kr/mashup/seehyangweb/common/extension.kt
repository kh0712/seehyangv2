package kr.mashup.seehyangweb.common

fun Any.toJson(): String {
    return DEFAULT_OBJECT_MAPPER.writeValueAsString(this)
}