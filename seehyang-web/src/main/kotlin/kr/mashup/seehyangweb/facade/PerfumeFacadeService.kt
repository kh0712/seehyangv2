package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.*
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangcore.vo.PerfumeType
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

@NonTransactionalService
class PerfumeFacadeService(
    private val communityService: CommunityService,
    private val perfumeService: PerfumeService,
) {
    fun getPerfume(userAuth: UserAuth, perfumeId: Long): PerfumeDetailInfoResponse {

        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)
        val likeCount = perfumeService.getLikeCountById(perfumeId)
        val storyCount = communityService.getStoryCountByPerfume(perfumeId)

        return PerfumeDetailInfoResponse.from(perfumeInfo, likeCount, storyCount)
    }

    fun getPerfumes(userAuth: UserAuth, pageable: Pageable): Page<PerfumeBasicInfoResponse> {
        return perfumeService.getAll(pageable).map {
            PerfumeBasicInfoResponse.from(it)
        }
    }

    fun getPerfumesByName(name: String, pageable: Pageable): Page<PerfumeBasicInfoResponse> {
        return perfumeService
            .searchByPerfumeName(name, pageable)
            .map {
                PerfumeBasicInfoResponse.from(it)
            }
    }

    fun likePerfume(perfumeId: Long, userAuth: UserAuth) {
        perfumeService.likePerfume(userAuth.id, perfumeId)
    }
}

data class PerfumeBasicInfoResponse(
    val id: Long,
    val name: String,
    val koreanName: String,
    val type: PerfumeType,
    val gender: Gender,
    val thumbnailId: Long,
    val brand: BrandInfo,
){
    companion object {
        fun from(perfumeInfo: PerfumeInfo): PerfumeBasicInfoResponse {
            return PerfumeBasicInfoResponse(
                id = perfumeInfo.id,
                name = perfumeInfo.name,
                koreanName = perfumeInfo.koreanName,
                type = perfumeInfo.type,
                gender = perfumeInfo.gender,
                thumbnailId = perfumeInfo.thumbnailId,
                brand = perfumeInfo.brand
            )
        }
    }
}

data class PerfumeDetailInfoResponse(
    val id: Long,
    val name: String,
    val koreanName: String,
    val type: PerfumeType,
    val gender: Gender,
    val thumbnailId: Long,
    val brand: BrandInfo,
    val accords: List<AccordInfo>,
    val notes: List<NoteInfo>,
    val likeCount: Long,
    val storyCount: Long,
) {
    companion object {
        fun from(perfumeInfo: PerfumeInfo, likeCount: Long, storyCount: Long): PerfumeDetailInfoResponse {
            return PerfumeDetailInfoResponse(
                id = perfumeInfo.id,
                name = perfumeInfo.name,
                koreanName = perfumeInfo.koreanName,
                type = perfumeInfo.type,
                gender = perfumeInfo.gender,
                thumbnailId = perfumeInfo.thumbnailId,
                brand = perfumeInfo.brand,
                accords = perfumeInfo.accords,
                notes = perfumeInfo.notes,
                likeCount = likeCount,
                storyCount = storyCount
            )
        }
    }
}

