package kr.mashup.seehyangrds.perfume.domain

import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.InternalServerException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.perfume.entity.Perfume
import kr.mashup.seehyangrds.perfume.entity.PerfumeLike
import kr.mashup.seehyangrds.perfume.repo.PerfumeLikeRepository
import kr.mashup.seehyangrds.perfume.repo.PerfumeRepository
import kr.mashup.seehyangrds.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.regex.Pattern

@TransactionalService
class PerfumeDomain(
    private val perfumeRepository: PerfumeRepository,
    private val perfumeLikeRepository: PerfumeLikeRepository
) {

    fun getByIdOrThrow(perfumeId: Long): Perfume {
        if (perfumeId < 0) {
            throw BadRequestException(ResultCode.INVALID_PERFUME_ID)
        }
        return perfumeRepository.findById(perfumeId).orElseThrow { PERFUME_NOT_FOUND_EXCEPTION }
    }

    fun searchByName(name:String, pageable: Pageable): Page<Perfume> {
        return perfumeRepository.findByNameOrKoreanName(name, pageable)
    }

    fun likePerfume(perfumeId: Long, user: User) {
        val perfume = perfumeRepository.findById(perfumeId).orElseThrow { PERFUME_NOT_FOUND_EXCEPTION }
        if (perfumeLikeRepository.existsByPerfumeAndUser(perfume, user)) {
            throw BadRequestException(ResultCode.INVALID_PERFUME_LIKE_REQUEST)
        }
        val perfumeLike = PerfumeLike(user, perfume)
        perfumeLikeRepository.save(perfumeLike)
    }

    fun dislikePerfume(perfumeId: Long, user: User) {
        val perfume = perfumeRepository.findById(perfumeId).orElseThrow { PERFUME_NOT_FOUND_EXCEPTION }
        val perfumeLike = perfumeLikeRepository
            .findByUserAndPerfume(user, perfume)?: throw BadRequestException(ResultCode.INVALID_PERFUME_LIKE_REQUEST)

        perfumeLikeRepository.delete(perfumeLike)
    }

    fun getLikeCount(perfumeId: Long): Long {
        val perfume = perfumeRepository.findById(perfumeId).orElseThrow { PERFUME_NOT_FOUND_EXCEPTION }
        return perfumeLikeRepository.countByPerfume(perfume)
    }

    private fun isEnglishOrDigit(input: String): Boolean = Pattern.matches("^[a-zA-Z0-9]*$", input)
    private fun isKoreanOrDigit(input: String): Boolean = Pattern.matches("^[ㄱ-ㅎ|가-힣|0-9|]+$", input)

    private val PERFUME_NOT_FOUND_EXCEPTION = NotFoundException(ResultCode.NOT_FOUND_PERFUME)

}