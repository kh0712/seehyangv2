package kr.mashup.seehyangweb.api

import io.swagger.v3.oas.annotations.Parameter
import kr.mashup.seehyangbusiness.business.CommentSortRequest
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.CommentCreateRequest
import kr.mashup.seehyangweb.facade.CommentInfoResponse
import kr.mashup.seehyangweb.facade.CommunityFacadeService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@ApiV1
class CommentController(
    private val communityFacadeService: CommunityFacadeService
) {

    @GetMapping("/story/{storyId}/comment")
    fun getComments(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        commentSortRequest: CommentSortRequest
    ): SeehyangResponse<List<CommentInfoResponse>> {

        val commentsDto = communityFacadeService.getComments(userAuth, storyId, commentSortRequest)

        return SeehyangResponse.success(commentsDto)
    }


    @GetMapping("/story/{storyId}/comment/{id}")
    fun getReplyComments(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") parentCommentId: Long,
        commentSortRequest: CommentSortRequest
    ): SeehyangResponse<List<CommentInfoResponse>> {

        val replyComments =
            communityFacadeService.getReplyComments(userAuth, storyId, parentCommentId, commentSortRequest)

        return SeehyangResponse.success(replyComments)
    }


    @PostMapping("/story/{storyId}/comment")
    fun createComment(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.createComment(userAuth, storyId, request)

        return SeehyangResponse.success()
    }


    @PostMapping("/story/{storyId}/comment/{commentId}")
    fun createReplyComment(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.addReplyComment(userAuth, commentId, request)

        return SeehyangResponse.success()
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/like")
    fun createCommentLike(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.likeComment(userAuth, commentId)

        return SeehyangResponse.success()
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/dislike")
    fun createCommentDislike(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.dislikeComment(userAuth, commentId)

        return SeehyangResponse.success()
    }


    @DeleteMapping("/story/{storyId}/comment/{commentId}")
    fun deleteComment(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.deleteComment(userAuth, commentId)

        return SeehyangResponse.success()
    }
}