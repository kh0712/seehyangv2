package kr.mashup.seehyangweb.facade

import kr.mashup.seehyangbusiness.business.*
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.cache.CacheSupport
import kr.mashup.seehyangweb.common.NonTransactionalService
import org.springframework.data.domain.Page
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@NonTransactionalService
class CommunityFacadeService(
    private val communityService: CommunityService,
    private val userService: UserService,
    private val perfumeService: PerfumeService,
    private val cacheSupport: CacheSupport
) {


    fun getStoryDetail(userAuth: UserAuth, storyId: Long): StoryDetailInfoResponse {

        val userCache = cacheSupport.getUser(userAuth.id)

        val userInfo: UserInfo = if (userCache == null) {
            val foundUserInfo = userService.getActiveUserOrThrow(userAuth.id)
            cacheSupport.putUserAsync(foundUserInfo)
            foundUserInfo
        } else {
            userCache
        }
        val storyCache = cacheSupport.getStory(storyId)
        val storyInfo = if (storyCache == null) {
            val foundStoryInfo = communityService.getStoryInfoByStoryId(storyId)
            cacheSupport.putStoryAsync(foundStoryInfo)
            foundStoryInfo
        } else {
            storyCache
        }

        val viewType = storyInfo.viewType
        if (viewType == StoryViewType.ONLYADMIN || (viewType == StoryViewType.ONLYME && storyInfo.userId != userInfo.id)) {
            throw BadRequestException(ResultCode.NOT_FOUND_STORY)
        }

        val likeCountCache = cacheSupport.getStoryLike(storyId)
        val likeCount = if (likeCountCache == null) {
            val foundLikeCount = communityService.getActiveStoryLikeCount(storyId)
            cacheSupport.putStoryLikeAsync(storyId, foundLikeCount)
            foundLikeCount
        } else {
            likeCountCache
        }

        val perfumeId = storyInfo.perfumeId
        val perfumeCache = cacheSupport.getPerfume(perfumeId)
        val perfumeInfo = if (perfumeCache == null) {
            val foundPerfumeInfo = perfumeService.getByIdOrThrow(perfumeId)
            cacheSupport.putPerfumeAsync(foundPerfumeInfo)
            foundPerfumeInfo
        } else {
            perfumeCache
        }

        return StoryDetailInfoResponse.from(userInfo, perfumeInfo, storyInfo, likeCount)
    }

    fun getStoriesByPerfume(
        userAuth: UserAuth,
        perfumeId: Long,
        storySortRequest: StorySortRequest
    ): Page<StoryBasicInfoResponse> {

        val userInfo = userService.getActiveUserOrThrow(userAuth.id)
        val perfumeInfo = perfumeService.getByIdOrThrow(perfumeId)

        return communityService
            .getStoryInfoByPerfume(perfumeId, userAuth.id, storySortRequest)
            .map { StoryBasicInfoResponse.from(userInfo, perfumeInfo, it) }
    }

    fun createStory(userAuth: UserAuth, request: StoryCreateRequest) {

        communityService.createStory(
            userAuth.id, request.perfumeId, request.imageId, request.viewType
        )

    }

    fun likeStory(userAuth: UserAuth, storyId: Long) {

        communityService.likeOrCancelStory(storyId, userAuth.id)
    }

    fun deleteStory(storyId: Long, userAuth: UserAuth) {

        communityService.deleteStory(storyId, userAuth.id)
    }

    fun getComments(
        userAuth: UserAuth,
        storyId: Long,
        commentSortRequest: CommentSortRequest
    ): Page<CommentInfoResponse> {

        return communityService
            .getComments(userAuth.id, storyId, commentSortRequest)
            .map { CommentInfoResponse.from(userService.getActiveUserOrThrow(it.userId), it) }
    }

    fun getReplyComments(
        userAuth: UserAuth,
        storyId: Long,
        parentCommentId: Long,
        commentSortRequest: CommentSortRequest,
    ): Page<CommentInfoResponse> {

        return communityService
            .getReplyComments(userAuth.id, storyId, parentCommentId, commentSortRequest)
            .map { CommentInfoResponse.from(userService.getActiveUserOrThrow(it.userId), it) }
    }

    fun createComment(userAuth: UserAuth, storyId: Long, request: CommentCreateRequest) {

        communityService.createComment(storyId, userAuth.id, request.contents)
    }

    fun addReplyComment(
        userAuth: UserAuth,
        commentId: Long,
        request: CommentCreateRequest
    ) {
        communityService.createReply(userAuth.id, commentId, request.contents)
    }

    fun likeComment(userAuth: UserAuth, commentId: Long) {
        communityService.likeComment(userAuth.id, commentId)
    }

    fun dislikeComment(userAuth: UserAuth, commentId: Long) {
        communityService.dislikeComment(userAuth.id, commentId)
    }

    fun deleteComment(userAuth: UserAuth, commentId: Long) {
        communityService.deleteComments(userAuth.id, commentId)
    }
}

data class StoryBasicInfoResponse(
    val id: Long,
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
        fun from(userInfo: UserInfo, perfumeInfo: PerfumeInfo, storyInfo: StoryInfo): StoryBasicInfoResponse {
            return StoryBasicInfoResponse(
                id = storyInfo.id,
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

data class StoryDetailInfoResponse(
    val id: Long,
    val userId: Long,
    val userNickname: String,
    val userProfileId: Long?,
    val perfumeId: Long,
    val perfumeName: String,
    val perfumeKoreanName: String,
    val perfumeThumbnailId: Long,
    val storyImageId: Long,
    val viewType: StoryViewType,
    val likeCount: Long,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime,
) {
    companion object {
        fun from(
            userInfo: UserInfo,
            perfumeInfo: PerfumeInfo,
            storyInfo: StoryInfo,
            likeCount: Long
        ): StoryDetailInfoResponse {
            return StoryDetailInfoResponse(
                id = storyInfo.id,
                userId = userInfo.id!!,
                userNickname = userInfo.nickname,
                userProfileId = userInfo.profileUrlId,
                perfumeId = perfumeInfo.id,
                perfumeName = perfumeInfo.name,
                perfumeKoreanName = perfumeInfo.koreanName,
                perfumeThumbnailId = perfumeInfo.thumbnailId,
                storyImageId = storyInfo.imageId,
                viewType = storyInfo.viewType,
                likeCount = likeCount,
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

        fun from(userInfo: UserInfo, commentReplyInfo: CommentReplyInfo): CommentInfoResponse {
            return CommentInfoResponse(
                commentReplyInfo.id!!,
                userInfo.id!!,
                userInfo.nickname,
                userInfo.profileUrlId,
                commentReplyInfo.contents,
                commentReplyInfo.createdAt,
                commentReplyInfo.updatedAt
            )
        }
    }
}

data class CommentCreateRequest(

    @NotBlank
    val contents: String
)