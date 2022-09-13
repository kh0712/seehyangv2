package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.*
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangcore.vo.PerfumeType
import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangweb.cache.CacheSupport
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.WeekFields
import java.util.*


@NonTransactionalService
class HomeFacadeService(
    private val userService: UserService,
    private val perfumeService: PerfumeService,
    private val communityService: CommunityService,
    private val homeService: HomeService
) {
    // 향수 중 아무거나
    fun todaySeehyang(): TodaySeehyangResponse {
        val today = LocalDate.now()
        val zoneId = ZoneId.systemDefault()
        val epoch: Long = today.atStartOfDay(zoneId).toEpochSecond()
        val perfumeId = (epoch % 100) + 1
        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)

        return TodaySeehyangResponse.from(perfumeInfo)
    }

    // 좋아요 많은 스토리 가져온다. - 배치 적용 완료
    fun hotStory(): List<StoryBasicInfoResponse> {
        val storyIds = homeService.getStoryIds(LocalDate.now(), PageRequest.of(0, 10, Sort.Direction.DESC, "likeCount"))
        val stories = communityService.getStoryInfoByStoryIds(storyIds)
        val userIds = stories.map { it.userId }.toList()
        val perfumeIds = stories.map { it.perfumeId }.toList()

        val userInfos: List<UserInfo> = userService.getActiveUsers(userIds)
        val perfumeInfos:List<PerfumeInfo> = perfumeService.getPerfumes(perfumeIds)

        val result = mutableListOf<StoryBasicInfoResponse>()

        for(storyInfo in stories){
            val userInfo = userInfos.firstOrNull{ it.id == storyInfo.userId}?: UserInfo(0L, "deleted", "deleted", 0, Gender.NULL, 0)
            val perfumeInfo = perfumeInfos.first{ it.id == storyInfo.perfumeId}
            result.add(StoryBasicInfoResponse.from(userInfo, perfumeInfo, storyInfo))
        }

        return result

    }

    // 저번주 스토리 많이 올라온 향수를 가져온다.
    fun weeklyRanking(): List<PerfumeInfo> {
        // TODO : 배치 적용하기
        val beforeWeek = LocalDate.now().atStartOfDay().minusWeeks(1L)
        val fieldISO = WeekFields.of(Locale.KOREA).dayOfWeek()
        val pageRequest = PageRequest.of(0, 10)

        return communityService.getMostStoriesPerfumes(
            beforeWeek.with(fieldISO, 1),
            beforeWeek.with(fieldISO, 7),
            pageRequest
        )
    }

    // 전체에서 스토리 많이 올라온 향수를 가져온다. 일주일 기준 변경
    fun getSteadyPerfumes(): List<PerfumeInfo> {
        // TODO : 배치 적용하기
        val pageRequest = PageRequest.of(0, 10)
        return communityService.getMostStoriesPerfumes(pageRequest)
    }
}

data class TodaySeehyangResponse(
    val id: Long,
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
        fun from(info: PerfumeInfo): TodaySeehyangResponse {
            return TodaySeehyangResponse(
                info.id!!,
                info.name,
                info.koreanName,
                info.type,
                info.gender,
                info.thumbnailId,
                info.brand,
                info.accords,
                info.notes
            )
        }
    }

}

data class HotStoryResponse(
    val id: Long,
    val userId: Long,
    val perfumeId: Long,
    val imageId: Long,
    val viewType: StoryViewType,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
)
