package kr.mashup.seehyangweb.api

import kr.mashup.seehyangbusiness.business.PerfumeInfo
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.*
import org.springframework.web.bind.annotation.GetMapping
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class HomeController(
    private val homeFacadeService: HomeFacadeService
) {
    @GetMapping("/home/today")
    fun today(
        @ApiIgnore userAuth: UserAuth
    ): SeehyangResponse<TodaySeehyangResponse> {

        val todaySeehyangResponse = homeFacadeService.todaySeehyang()
        return SeehyangResponse.success(todaySeehyangResponse)
    }

    @GetMapping("/home/hot")
    fun hotStory(
        @ApiIgnore userAuth: UserAuth
    ): SeehyangResponse<List<StoryBasicInfoResponse>> {

        val hotStories = homeFacadeService.hotStory()
        return SeehyangResponse.success(hotStories)
    }

    @GetMapping("/home/weekly")
    fun weeklyRanking(
        @ApiIgnore userAuth: UserAuth
    ): SeehyangResponse<List<PerfumeInfo>> {

        val weeklyPerfumes = homeFacadeService.weeklyRanking()
        return SeehyangResponse.success(weeklyPerfumes)
    }

    @GetMapping("/home/steady")
    fun steadyPerfume(
        @ApiIgnore userAuth: UserAuth,
    ): SeehyangResponse<List<PerfumeInfo>> {

        val steadyPerfumeResponses =  homeFacadeService.getSteadyPerfumes()
        return SeehyangResponse.success(steadyPerfumeResponses)
    }

}