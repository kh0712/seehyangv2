package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangrds.community.domain.StoryDomain
import kr.mashup.seehyangrds.community.entity.Story
import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.community.domain.StorySaveCommand
import kr.mashup.seehyangrds.community.entity.Comment
import kr.mashup.seehyangrds.community.entity.CommentReply
import kr.mashup.seehyangrds.image.domain.ImageDomain
import kr.mashup.seehyangrds.perfume.domain.PerfumeDomain
import kr.mashup.seehyangrds.user.service.UserQueryDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@TransactionalService
class CommunityService(
    private val perfumeDomain: PerfumeDomain,
    private val userQueryDomain: UserQueryDomain,
    private val storyDomain: StoryDomain,
    private val imageDomain: ImageDomain
) {
    fun getPublicStories(storySortRequest: StorySortRequest):List<StoryInfo> {
        val sortBy = storySortRequest.sortBy ?: StorySortBy.ID
        val direction = storySortRequest.direction ?: Sort.Direction.DESC
        val page = storySortRequest.page ?: 0
        val size = storySortRequest.size ?: 10
        val pageRequest = PageRequest.of(page, size, direction, sortBy.fieldName)

        return if (sortBy == StorySortBy.ID || sortBy == StorySortBy.RECENT) {
            val pageRequest = PageRequest.of(page, size, direction, sortBy.fieldName)
            storyDomain.getPublicStories(pageRequest)
        } else {
            val pageRequest = PageRequest.of(page, size)
            storyDomain.getPublicStoriesOrderByLike(pageRequest)
        }.map { StoryInfo.from(it) }
    }

    fun getStoryInfoByStoryId(storyId: Long, userId: Long): StoryInfo {

        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)

        val accessibility = story.viewType

        return when (accessibility) {
            StoryViewType.PUBLIC -> StoryInfo.from(story)
            StoryViewType.ONLYADMIN -> throw BadRequestException(ResultCode.NOT_FOUND_STORY)
            StoryViewType.ONLYME -> {
                if (story.user.id!! == user.id!!) {
                    StoryInfo.from(story)
                } else {
                    throw BadRequestException(ResultCode.NOT_FOUND_STORY)
                }
            }
        }
    }


    fun getStoryInfoByPerfume(perfumeId: Long, userId: Long, storySortRequest: StorySortRequest): Page<StoryInfo> {

        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val sortBy = storySortRequest.sortBy ?: StorySortBy.ID
        val direction = storySortRequest.direction ?: Sort.Direction.DESC
        val page = storySortRequest.page ?: 0
        val size = storySortRequest.size ?: 10

        return if (sortBy == StorySortBy.ID || sortBy == StorySortBy.RECENT) {
            val pageRequest = PageRequest.of(page, size, direction, sortBy.fieldName)
            storyDomain.getAccessibleByUserAndPerfume(user, perfume, pageRequest)
        } else {
            val pageRequest = PageRequest.of(page, size)
            storyDomain.getByPerfumeOrderByLike(user, perfume, pageRequest)
        }.map { StoryInfo.from(it) }


    }

    fun createStory(userId: Long, perfumeId: Long, imageId: Long, viewType: StoryViewType): StoryInfo {

        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        val image = imageDomain.getByIdOrThrow(imageId)

        if (image.status != ImageStatus.PENDING) {
            throw BadRequestException(ResultCode.ALREADY_EXIST_IMAGE)
        }
        if(image.uploader.id!! != user.id ){
            throw NotFoundException(ResultCode.NOT_FOUND_IMAGE)
        }
        imageDomain.changeStatus(image, ImageStatus.ACTIVE)
        val story = storyDomain.saveStory(StorySaveCommand(user, perfume, image, viewType))

        return StoryInfo.from(story)
    }

    fun likeOrCancelStory(storyId: Long, userId: Long) {
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        storyDomain.likeOrCancelStory(user, story)
    }

    fun deleteStory(storyId: Long, userId: Long) {
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        if (story.user != user) {
            throw BadRequestException(ResultCode.NOT_FOUND_STORY)
        }
        storyDomain.deleteStory(story)
    }

    fun createComment(storyId: Long, userId: Long, contents: String): CommentInfo {
        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        return CommentInfo.from(storyDomain.createComment(user, story, contents))
    }

    fun getComments(userId:Long, storyId: Long, commentSortRequest: CommentSortRequest): Page<CommentInfo> {

        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        if(story.viewType == StoryViewType.ONLYME && userId != story.user.id!!){
            throw NotFoundException(ResultCode.NOT_FOUND_STORY)
        }
        val sortBy = commentSortRequest.sortBy ?: CommentSortBy.ID
        val direction = commentSortRequest.direction ?: Sort.Direction.DESC
        val page = commentSortRequest.page ?: 0
        val size = commentSortRequest.size ?: 10

        return if (sortBy == CommentSortBy.ID || sortBy == CommentSortBy.RECENT) {
            val pageRequest = PageRequest.of(page, size, direction, sortBy.fieldName)
            storyDomain.getActiveComments(story, pageRequest)
        } else {
            val pageRequest = PageRequest.of(page, size)
            storyDomain.getActiveComments(story, pageRequest)
        }.map { CommentInfo.from(it) }
    }

    fun deleteComments(userId: Long, commentId: Long) {
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val comment = storyDomain.getActiveComment(commentId)
        if (user != comment.user) {
            throw BadRequestException(ResultCode.NOT_FOUND_COMMENT)
        }
        storyDomain.deleteComment(comment)
    }

    fun createReply(userId:Long, commentId: Long, contents: String):CommentReplyInfo{
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val comment = storyDomain.getActiveComment(commentId)
        val commentReply = storyDomain.createReply(user, comment, contents)

        return CommentReplyInfo.from(commentReply)
    }

    fun likeComment(userId: Long, commentId: Long) {
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val comment = storyDomain.getActiveComment(commentId)

        storyDomain.likeComment(user, comment)
    }

    fun dislikeComment(userId: Long, commentId: Long) {
        val user = userQueryDomain.getActiveByIdOrThrow(userId)
        val comment = storyDomain.getActiveComment(commentId)

        storyDomain.dislikeComment(user, comment)
    }

    fun getReplyComments(userId:Long, storyId: Long,parentCommentId:Long, commentSortRequest: CommentSortRequest): Page<CommentReplyInfo> {

        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        val comment = storyDomain.getActiveComment(parentCommentId)


        if(story.viewType == StoryViewType.ONLYME && userId != story.user.id!!){
            throw NotFoundException(ResultCode.NOT_FOUND_STORY)
        }

        val sortBy = commentSortRequest.sortBy ?: CommentSortBy.ID
        val direction = commentSortRequest.direction ?: Sort.Direction.DESC
        val page = commentSortRequest.page ?: 0
        val size = commentSortRequest.size ?: 10
        return if (sortBy == CommentSortBy.ID || sortBy == CommentSortBy.RECENT) {
            val pageRequest = PageRequest.of(page, size, direction, sortBy.fieldName)
            storyDomain.getActiveReplies(comment, pageRequest)
        } else {
            val pageRequest = PageRequest.of(page, size)
            storyDomain.getActiveReplies(comment, pageRequest)
        }.map { CommentReplyInfo.from(it) }
    }

    @Transactional(readOnly = true)
    fun getMostStoriesPerfumes(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): List<PerfumeInfo> {
        return storyDomain
            .getPerfumeIdsByMostStories(from, to, pageable)
            .map{perfumeDomain.getByIdOrThrow(it)}
            .map{PerfumeInfo.from(it)}
    }

    fun getMostStoriesPerfumes(pageable:Pageable): List<PerfumeInfo> {
        return storyDomain
            .getPerfumeIdsByMostStories(pageable)
            .map{perfumeDomain.getByIdOrThrow(it)}
            .map{PerfumeInfo.from(it)}
    }

    fun getStoryCountByPerfume(perfumeId: Long): Long {
        val perfume = perfumeDomain.getByIdOrThrow(perfumeId)
        return storyDomain.getActiveStoryCountByPerfume(perfume)
    }

    fun getActiveStoryLikeCount(storyId: Long):Long {
        val story = storyDomain.getActiveStoryByIdOrThrow(storyId)
        return storyDomain.getLikeCount(story)
    }


}

data class StoryInfo(
    val id: Long,
    val userId: Long,
    val perfumeId: Long,
    val imageId: Long,
    val viewType: StoryViewType,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
) {
    companion object {
        fun from(story: Story): StoryInfo {
            return StoryInfo(
                id = story.id!!,
                userId = story.user.id!!,
                perfumeId = story.perfume.id!!,
                imageId = story.image.id!!,
                viewType = story.viewType,
                createdAt = story.createdAt,
                modifiedAt = story.updatedAt
            )
        }
    }
}

data class StoryCreateCommand(
    val userId: Long,
    val perfumeId: Long,
    val imageId: Long,
    val storyViewType: StoryViewType
)

data class StorySortRequest(
    val page: Int?,
    val size: Int?,
    val sortBy: StorySortBy?,
    val direction: Sort.Direction?,
)

data class CommentInfo(
    val id: Long,
    val userId: Long,
    val storyId: Long,
    val contents: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(comment: Comment): CommentInfo {
            return CommentInfo(
                id = comment.id!!,
                userId = comment.user.id!!,
                storyId = comment.story.id!!,
                contents = comment.contents,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt
            )
        }
    }
}
data class CommentReplyInfo(
    val id: Long,
    val userId: Long,
    val parentId: Long,
    val contents: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(commentReply: CommentReply): CommentReplyInfo {
            return CommentReplyInfo(
                id = commentReply.id!!,
                userId = commentReply.user.id!!,
                parentId = commentReply.comment.id!!,
                contents = commentReply.contents,
                createdAt = commentReply.createdAt,
                updatedAt = commentReply.updatedAt
            )
        }
    }
}


data class CommentSortRequest(
    val page: Int?,
    val size: Int?,
    val sortBy: CommentSortBy?,
    val direction: Sort.Direction?,
)

enum class StorySortBy(
    val fieldName: String
) {
    ID("id"), LIKE("like"), RECENT("created_at")
}

enum class CommentSortBy(
    val fieldName: String
) {
    ID("id"), LIKE("like"), RECENT("created_at")
}