package kr.mashup.seehyangrds.user.service

import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.user.entity.User
import kr.mashup.seehyangrds.user.entity.UserStatus
import kr.mashup.seehyangrds.user.repo.UserRepository


@TransactionalService
class UserQueryDomain(
    private val userRepository: UserRepository
) {

    fun getActiveByIdOrThrow(id: Long): User =
        userRepository.findById(id).filter { it.status == UserStatus.ACTIVE }.orElseThrow { NOT_FOUND_USER_EXCEPTION }

    fun existById(id: Long): Boolean = userRepository.existsById(id)

    fun getActiveByEmailOrThrow(email: String): User =
        userRepository.findByEmail(email).takeIf { it?.status == UserStatus.ACTIVE } ?: throw NOT_FOUND_USER_EXCEPTION

    fun existByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    fun getActiveByNicknameOrThrow(nickname: String): User =
        userRepository.findByNickname(nickname).takeIf { it?.status == UserStatus.ACTIVE }
            ?: throw NOT_FOUND_USER_EXCEPTION

    fun existByNickname(nickname: String): Boolean = userRepository.existsByNickname(nickname)

    fun getActiveByAge(age: Int): User? = userRepository.findByAge(age).takeIf { it?.status == UserStatus.ACTIVE }

    fun getByStatus(status: UserStatus): List<User> = userRepository.findByStatus(status)

    private val NOT_FOUND_USER_EXCEPTION = NotFoundException(ResultCode.NOT_FOUND_USER)

}