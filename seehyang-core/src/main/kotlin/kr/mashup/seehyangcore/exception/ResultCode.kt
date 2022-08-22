package kr.mashup.seehyangcore.exception

enum class ResultCode(
    val code: Int,
    val message: String
) {
    // common
    OK(2000, "OK"),
    INVALID_CURSOR_PARAMETER(4001, "You need like cursor and id cursor both."),
    INVALID_PAGING_PARAMETER(0,"invalid paging param"),
    INVALID_IMAGE(0, "invalid image format"),
    BAD_REQUEST(0," bad request"),
    // user
    NOT_FOUND_USER(4040, "The user does not exist."),
    ALREADY_EXIST_USER(4002, "Already exist user."),
    INVALID_NICKNAME(4006,"Nickname must be English or Korean or Number"),
    INVALID_EMAIL(4007,"Invalid Email"),
    INVALID_PASSWORD(0,"wrong password"),

    // perfume
    INVALID_PERFUME_ID(0, "invalid perfume id"),
    INVALID_PERFUME_EDIT_REQUEST(4009, "At least one filed must not be null"),
    INVALID_PERFUME_NAME(0, "invalid perfume name"),
    INVALID_PERFUME_LIKE_REQUEST(0,"already exist like"),

    // community

    CONTENTS_IS_EMPTY(4004, "Contents is empty or blank"),
    INVALID_AGE(4005,"Age must be in range 0 to 100"),

    INVALID_CREATE_REQUEST(4008, "Necessary field is empty "),
    INVALID_COMMENT_REPLY_REQUEST(40010,"Cannot Reply to Reply Comment"),



    UNAUTHORIZED_USER(4010, "Unauthenticated user."),
    UNAUTHORIZED_INVALID_TOKEN(4011, "Invalid token."),

    NOT_FOUND_COMMENT(4041, "You need to write the content."),
    NOT_FOUND_STORY(4042, "The story does not exist."),
    NOT_FOUND_PERFUME(4043,"The perfume does not exist."),
    NOT_FOUND_STORYLIKE(4044,"The story like does not exist"),
    NOT_FOUND_IMAGE(4045,"The image does not exist."),
    NOT_FOUND_BRNAD(4046,"The brand does not exist."),

    INTERNAL_SERVER_ERROR(5000, "This request cannot be proceed."),
    INVALID_USER_ENTITY(5001, "Invalid User entity for making User Dto"),
    INVALID_IMAGE_ENTITY(5002, "Invalid Image Entity"),
    INVALID_STORY_ENTITY(5003, "Invalid Story Entity"),
    INVALID_TAG_ENTITY(5004, "Invalid Tag Entity"),
    ALREADY_EXIST_IMAGE(0, "already exist image"),
    ALREADY_EXIST_STORYLIKE(0,"already exist story like"),
    UNAUTHORIZED(0, "unauthorized"),
    FORBIDDEN(0, "forbidden"),
    ALREADY_EXIST_NICKNAME(0, "already exist nickname"),
    UNPAGED_ERROR(0, "should paged");


    companion object {
        fun findByCode(code: Int) = values().first { it.code == code }
    }
}