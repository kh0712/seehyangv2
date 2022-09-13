package kr.mashup.seehyangweb.api

import io.swagger.v3.oas.annotations.Parameter
import kr.mashup.seehyangbusiness.business.PerfumeInfo
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.HomeFacadeService
import kr.mashup.seehyangweb.facade.StoryBasicInfoResponse
import kr.mashup.seehyangweb.facade.TodaySeehyangResponse
import org.springframework.web.bind.annotation.GetMapping

@ApiV1
class HomeController(
    private val homeFacadeService: HomeFacadeService
) {
    @GetMapping("/home/today")
    fun today(
        @Parameter(hidden = true) userAuth: UserAuth
    ): SeehyangResponse<TodaySeehyangResponse> {

        val todaySeehyangResponse = homeFacadeService.todaySeehyang()
        return SeehyangResponse.success(todaySeehyangResponse)
    }

    @GetMapping("/home/hot")
    fun hotStory(
        @Parameter(hidden = true) userAuth: UserAuth
    ): SeehyangResponse<List<StoryBasicInfoResponse>> {

        val hotStories = homeFacadeService.hotStory()
        return SeehyangResponse.success(hotStories)
    }

    @GetMapping("/home/weekly")
    fun weeklyRanking(
        @Parameter(hidden = true) userAuth: UserAuth
    ): SeehyangResponse<List<PerfumeInfo>> {

        val weeklyPerfumes = homeFacadeService.weeklyRanking()
        return SeehyangResponse.success(weeklyPerfumes)
    }

    @GetMapping("/home/steady")
    fun steadyPerfume(
        @Parameter(hidden = true) userAuth: UserAuth,
    ): SeehyangResponse<List<PerfumeInfo>> {

        val steadyPerfumeResponses =  homeFacadeService.getSteadyPerfumes()
        return SeehyangResponse.success(steadyPerfumeResponses)
    }

}