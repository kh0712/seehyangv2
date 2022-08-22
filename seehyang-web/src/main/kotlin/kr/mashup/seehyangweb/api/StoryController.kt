package kr.mashup.seehyangweb.api

import kr.mashup.seehyangbusiness.business.StorySortRequest
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.StoryCreateRequest
import kr.mashup.seehyangweb.facade.CommunityFacadeService
import kr.mashup.seehyangweb.facade.StoryBasicInfoResponse
import kr.mashup.seehyangweb.facade.StoryDetailInfoResponse
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@ApiV1
class StoryController(
    private val communityFacadeService: CommunityFacadeService
) {

    /**
     * 1. 스토리 id로 하나 가져오기
     */
    @GetMapping("/story/{storyId}")
    fun getStory(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable("storyId") storyId: Long,
    ): SeehyangResponse<StoryDetailInfoResponse> {

        val storyDto = communityFacadeService.getStoryDetail(userAuth, storyId)
        return SeehyangResponse.success(storyDto)
    }


    /**
     * 2. 향수 id 로 여러개 가져오기
     */
    @GetMapping("/perfume/{perfumeId}/story")
    fun getStoriesByPerfume(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "perfumeId") perfumeId: Long,
        storySortRequest: StorySortRequest,
    ): SeehyangResponse<List<StoryBasicInfoResponse>> {
        val storyListDto = communityFacadeService.getStoriesByPerfume(userAuth, perfumeId, storySortRequest)
        return SeehyangResponse.success(storyListDto)
    }


    @PostMapping("/story")
    fun createStory(
        @ApiIgnore userAuth: UserAuth,
        @Valid @RequestBody createRequest: StoryCreateRequest,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.createStory(userAuth, createRequest)
        return SeehyangResponse.success()
    }


    @PostMapping("/story/{storyId}/like")
    fun likeStory(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable("storyId") storyId: Long,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.likeStory(userAuth,storyId)
        return SeehyangResponse.success()
    }

    @DeleteMapping("/story/{storyId}")
    fun deleteStory(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable storyId: Long
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.deleteStory(storyId, userAuth)
        return SeehyangResponse.success()
    }
}

