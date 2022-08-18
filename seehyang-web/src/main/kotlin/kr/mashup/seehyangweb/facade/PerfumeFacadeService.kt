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
    fun getPerfume(perfumeId: Long, userAuth: UserAuth): PerfumeInfoResponse {

        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)
        val likeCount = perfumeService.getLikeCountById(perfumeId)
        val storyCount = communityService.getStoryCountByPerfume(perfumeId)

        return PerfumeInfoResponse.from(perfumeInfo, likeCount, storyCount)
    }

    fun getPerfumesByName(name: String, pageable: Pageable): Page<PerfumeInfoResponse> {
        return perfumeService
            .searchByPerfumeName(name, pageable)
            .map { PerfumeInfoResponse.from(it,perfumeService.getLikeCountById(it.id),
                                            communityService.getStoryCountByPerfume(it.id) ) }
    }

    fun likePerfume(perfumeId: Long, userAuth: UserAuth) {
        perfumeService.likePerfume(userAuth.id,perfumeId)
    }
}

data class PerfumeInfoResponse(
    val id:Long,
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
){
    companion object{
        fun from(perfumeInfo: PerfumeInfo, likeCount: Long, storyCount:Long):PerfumeInfoResponse{
            val (id, name, koreanName, type, gender, thumbnailId, brand, accords, notes) = perfumeInfo.copy()
            return PerfumeInfoResponse(id,name,koreanName,type,gender,thumbnailId,brand,accords,notes,likeCount, storyCount)
        }
    }
}

