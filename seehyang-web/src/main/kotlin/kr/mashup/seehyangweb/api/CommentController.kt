package kr.mashup.seehyangweb.api

import kr.mashup.seehyangbusiness.business.CommentSortRequest
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.*
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@ApiV1
class CommentController(
    private val communityFacadeService: CommunityFacadeService
) {

    @GetMapping("/story/{storyId}/comment")
    fun getComments(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @RequestParam(value = "cursor", required = false) cursor: Long? = null,
        commentSortRequest: CommentSortRequest
    ): SeehyangResponse<List<CommentInfoResponse>> {

        val commentsDto = communityFacadeService.getComments(storyId, userAuth, commentSortRequest)

        return SeehyangResponse.success(commentsDto)
    }


    @GetMapping("/story/{storyId}/comment/{id}")
    fun getReplyComments(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") parentCommentId: Long,
        commentSortRequest: CommentSortRequest
    ): SeehyangResponse<List<CommentInfoResponse>> {

        val replyComments = communityFacadeService.getReplyComments(storyId, parentCommentId, userAuth, commentSortRequest)

        return SeehyangResponse.success(replyComments)
    }



    @PostMapping("/story/{storyId}/comment")
    fun createComment(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.createComment(storyId, userAuth, request)

        return SeehyangResponse.success()
    }


    @PostMapping("/story/{storyId}/comment/{commentId}")
    fun createReplyComment(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
        @Valid @RequestBody request: CommentCreateRequest,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.addReplyComment(commentId, userAuth, request)

        return SeehyangResponse.success()
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/like")
    fun createCommentLike(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.likeComment(commentId, userAuth)

        return SeehyangResponse.success()
    }

    @PostMapping("/story/{storyId}/comment/{commentId}/dislike")
    fun createCommentDislike(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.dislikeComment(commentId, userAuth)

        return SeehyangResponse.success()
    }


    @DeleteMapping("/story/{storyId}/comment/{commentId}")
    fun deleteComment(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable(value = "storyId") storyId: Long,
        @PathVariable(value = "commentId") commentId: Long,
    ): SeehyangResponse<EmptyResponse> {

        communityFacadeService.deleteComment(commentId,userAuth)

        return SeehyangResponse.success()
    }
}