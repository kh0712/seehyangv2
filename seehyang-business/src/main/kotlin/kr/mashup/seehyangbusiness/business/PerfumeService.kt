package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangcore.vo.PerfumeType
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.perfume.domain.PerfumeDomain
import kr.mashup.seehyangrds.perfume.entity.*
import kr.mashup.seehyangrds.user.service.UserQueryDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@TransactionalService
class PerfumeService(
    private val userQueryDomain: UserQueryDomain,
    private val perfumeDomain: PerfumeDomain
) {
    // 조회
    fun getByIdOrThrow(perfumeId: Long): PerfumeInfo {

        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        return PerfumeInfo.from(perfume)
    }
    fun getLikeCountById(perfumeId: Long):Long{
        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        return perfumeDomain.getLikeCount(perfume)
    }
    fun searchByPerfumeName(name: String, pageable: Pageable): Page<PerfumeInfo> {
        return perfumeDomain.searchByName(name, pageable).map { PerfumeInfo.from(it) }
    }

    fun getAll(pageable: Pageable): Page<PerfumeInfo> {
        return perfumeDomain.getAll(pageable).map { PerfumeInfo.from(it) }
    }

    // 변경
    fun likePerfume(userId:Long, perfumeId:Long){
        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        perfumeDomain.likePerfume(perfume, user)
    }

    fun getPerfumes(perfumeIds: List<Long>): List<PerfumeInfo> {
        return perfumeDomain.getPerfumes(perfumeIds).map{PerfumeInfo.from(it)}.toList()
    }

}

data class PerfumeInfo(
    val id:Long,
    val name: String,
    val koreanName: String,
    val type: PerfumeType,
    val gender: Gender,
    val thumbnailId: Long,
    val brand: BrandInfo,
    val accords: List<AccordInfo>,
    val notes: List<NoteInfo>,
) {
    companion object {
        fun from(perfume: Perfume): PerfumeInfo {
            return PerfumeInfo(
                id= perfume.id!!,
                name = perfume.name,
                koreanName = perfume.koreanName,
                type = perfume.type,
                gender = perfume.gender,
                thumbnailId = perfume.thumbnail.id!!,
                brand = BrandInfo(perfume.brand.id!!, perfume.brand.name, perfume.brand.koreanName),
                accords = perfume.accords.map { it.accord }.map { AccordInfo(it.id!!, it.name, it.koreanName) }
                    .toList(),
                notes = perfume.notes.map { it.note }.map { NoteInfo(it.id!!, it.name, it.koreanName) }
            )
        }
    }
}

data class NoteInfo(
    val id: Long,
    val name: String,
    val koreanName: String
)

data class AccordInfo(
    val id: Long,
    val name: String,
    val koreanName: String
)

data class BrandInfo(
    val id: Long,
    val name: String,
    val koreanName: String
)