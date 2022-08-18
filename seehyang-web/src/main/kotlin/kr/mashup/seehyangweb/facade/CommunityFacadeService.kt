package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.*
import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.data.domain.Page
import java.time.LocalDateTime

@NonTransactionalService
class CommunityFacadeService(
    private val communityService: CommunityService,
    private val userService: UserService,
    private val perfumeService: PerfumeService
) {

    fun getStoryDetail(storyId: Long, userAuth: UserAuth): StoryInfoResponse {
        val storyInfo = communityService.getStoryInfoByStoryId(storyId, userAuth.id)
        val userInfo = userService.getByIdOrThrow(userAuth.id)
        val perfumeId = storyInfo.perfumeId
        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)

        return StoryInfoResponse.from(userInfo, perfumeInfo, storyInfo)
    }

    fun getStoriesByPerfume(
        perfumeId: Long,
        userAuth: UserAuth,
        storySortRequest: StorySortRequest
    ): Page<StoryInfoResponse> {
        val userInfo = userService.getByIdOrThrow(userAuth.id)
        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)

        return communityService
            .getStoryInfoByPerfume(perfumeId, userAuth.id, storySortRequest)
            .map { StoryInfoResponse.from(userInfo, perfumeInfo, it) }
    }

    fun createStory(userAuth: UserAuth, request: StoryCreateRequest) {
        communityService.createStory(
            userAuth.id, request.perfumeId, request.imageId, request.viewType
        )

    }

    fun likeStory(storyId: Long, userAuth: UserAuth) {
        communityService.likeStory(storyId, userAuth.id)
    }

    fun deleteStory(storyId: Long, userAuth: UserAuth) {
        communityService.deleteStory(storyId, userAuth.id)
    }

    fun getComments(
        storyId: Long,
        userAuth: UserAuth,
        commentSortRequest: CommentSortRequest
    ): Page<CommentInfoResponse> {
        return communityService
            .getComments(userAuth.id,storyId, commentSortRequest)
            .map { CommentInfoResponse.from(userService.getByIdOrThrow(it.userId),it) }
    }

    fun getReplyComments(
        storyId: Long,
        parentCommentId: Long,
        userAuth: UserAuth,
        commentSortRequest: CommentSortRequest,
    ): Page<CommentInfoResponse> {
        return communityService
            .getReplyComments(userAuth.id, storyId, parentCommentId, commentSortRequest)
            .map { CommentInfoResponse.from(userService.getByIdOrThrow(it.userId),it) }
    }

    fun createComment(storyId: Long, userAuth: UserAuth, request: CommentCreateRequest) {
        communityService.createComment(storyId, userAuth.id, request.contents)
    }

    fun addReplyComment(
        commentId: Long,
        userAuth: UserAuth,
        request: CommentCreateRequest
    ) {
        communityService.createReply(userAuth.id, commentId, request.contents)
    }

    fun likeComment(commentId: Long, userAuth: UserAuth) {
        communityService.likeComment(userAuth.id, commentId)
    }

    fun dislikeComment(commentId: Long, userAuth: UserAuth) {
        communityService.dislikeComment(userAuth.id, commentId)
    }

    fun deleteComment(commentId: Long, userAuth: UserAuth) {
        communityService.deleteComments(userAuth.id, commentId)
    }
}

data class StoryInfoResponse(
    val userId: Long,
    val userNickname: String,
    val userProfileId: Long?,
    val perfumeId: Long,
    val perfumeName: String,
    val perfumeKoreanName: String,
    val perfumeThumbnailId: Long,
    val storyImageId: Long,
    val viewType: StoryViewType,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun from(userInfo: UserInfo, perfumeInfo: PerfumeInfo, storyInfo: StoryInfo): StoryInfoResponse {
            return StoryInfoResponse(
                userId = userInfo.id!!,
                userNickname = userInfo.nickname,
                userProfileId = userInfo.profileUrlId,
                perfumeId = perfumeInfo.id,
                perfumeName = perfumeInfo.name,
                perfumeKoreanName = perfumeInfo.koreanName,
                perfumeThumbnailId = perfumeInfo.thumbnailId,
                storyImageId = storyInfo.imageId,
                viewType = storyInfo.viewType,
                createdAt = storyInfo.createdAt,
                modifiedAt = storyInfo.modifiedAt
            )
        }
    }
}

data class StoryCreateRequest(
    val perfumeId: Long,
    val imageId: Long,
    val viewType: StoryViewType
)

data class CommentInfoResponse(
    val id: Long,
    val userId: Long,
    val userNickname: String,
    val userProfileId: Long?,
    val contents: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun from(userInfo: UserInfo, commentInfo: CommentInfo): CommentInfoResponse {
            return CommentInfoResponse(
                commentInfo.id!!,
                userInfo.id!!,
                userInfo.nickname,
                userInfo.profileUrlId,
                commentInfo.contents,
                commentInfo.createdAt,
                commentInfo.updatedAt
            )
        }

        fun from(userInfo:UserInfo, commentReplyInfo: CommentReplyInfo): CommentInfoResponse {
            return CommentInfoResponse(
                commentReplyInfo.id!!,
                userInfo.id!!,
                userInfo.nickname,
                userInfo.profileUrlId,
                commentReplyInfo.contents,
                commentReplyInfo.createdAt,
                commentReplyInfo.updatedAt)
        }
    }
}

data class CommentCreateRequest(
    val contents: String
)