package kr.mashup.seehyangcore.exception


class BadRequestException(status: ResultCode
): BaseException(status.code, status.message)

class UnauthorizedException(status: ResultCode
): BaseException(status.code, status.message)


class NotFoundException(status: ResultCode
): BaseException(status.code, status.message)

class InternalServerException(status: ResultCode
): BaseException(status.code, status.message)