package kr.mashup.seehyangrds.perfume.domain

import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.InternalServerException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.common.checkIsPaged
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
        checkIsPaged(pageable)
        return perfumeRepository.findByNameOrKoreanName(name, pageable)
    }

    fun isLikedPerfume(perfume:Perfume, user:User):Boolean{
        return perfumeLikeRepository.existsByPerfumeAndUser(perfume, user)
    }

    fun likePerfume(perfume:Perfume, user: User) {
        val perfumeLike = perfumeLikeRepository.findByUserAndPerfume(user, perfume)
        if (perfumeLike!=null) {
            perfumeLikeRepository.delete(perfumeLike)
        }else{
            val perfumeLike = PerfumeLike(user, perfume)
            perfumeLikeRepository.save(perfumeLike)
        }
    }


    fun getLikeCount(perfume:Perfume): Long {
        return perfumeLikeRepository.countByPerfume(perfume)
    }

    fun getAll(pageable: Pageable): Page<Perfume> {
        checkIsPaged(pageable)
        return perfumeRepository.findAll(pageable)
    }

    fun getPerfumes(perfumeIds: List<Long>): List<Perfume>{
        return perfumeRepository.findAllById(perfumeIds)
    }


    /**
     * Private
     */


    private val PERFUME_NOT_FOUND_EXCEPTION = NotFoundException(ResultCode.NOT_FOUND_PERFUME)

}