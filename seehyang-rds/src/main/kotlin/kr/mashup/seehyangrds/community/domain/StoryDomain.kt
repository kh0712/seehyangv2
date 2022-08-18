package kr.mashup.seehyangrds.community.domain

import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.NotFoundException
import kr.mashup.seehyangcore.exception.ResultCode
import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangrds.community.entity.*
import kr.mashup.seehyangrds.community.repo.*
import kr.mashup.seehyangrds.image.entity.Image
import kr.mashup.seehyangrds.perfume.entity.Perfume
import kr.mashup.seehyangrds.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import java.time.LocalDate

@Validated
@TransactionalService
class StoryDomain(
    private val storyRepository: StoryRepository,
    private val storyLikeRepository: StoryLikeRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val commentDislikeRepository: CommentDislikeRepository,
    private val replyRepository: CommentReplyRepository
) {
    fun saveStory(command: StorySaveCommand): Story {
        return storyRepository.save(
            Story(
                user = command.user,
                perfume = command.perfume,
                image = command.image,
                viewType = command.viewType
            )
        )
    }

    fun getActiveStoryByIdOrThrow(id: Long): Story {
        val story = storyRepository.findById(id).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_STORY) }
        if(story.status != StoryStatus.ACTIVE){
            throw NotFoundException(ResultCode.NOT_FOUND_STORY)
        }
        return story
    }

    fun getPublicStories(pageable: Pageable):List<Story>{
        return storyRepository.findPublicStories(pageable)
    }

    fun getStoryByIdOrThrow(id: Long):Story{
        return storyRepository.findById(id).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_STORY) }
    }
    // TODO: getActiveStoriesBy...
    fun getStoriesByUser(user: User, pageable: Pageable): Page<Story> {
        return storyRepository.findByUser(user, pageable)
    }

    fun getStoriesByPerfume(perfume: Perfume, pageable: Pageable): Page<Story> {
        return storyRepository.findByPerfume(perfume,pageable)
    }

    fun getAccessibleByUserAndPerfume(user:User, perfume: Perfume, pageable: Pageable): Page<Story>{
        return storyRepository.findAccessibleByUserAndPerfume(user, perfume, pageable)
    }

    fun getByPerfumeOrderByLike(user: User, perfume: Perfume, pageable: Pageable): Page<Story> {
        return storyRepository.findAccessibleByUserAndPerfumeOrderByLike(user, perfume, pageable)
    }


    fun likeStory(user: User, story: Story): StoryLike {
        if(isExist(user, story)){
            throw BadRequestException(ResultCode.ALREADY_EXIST_STORYLIKE)
        }
        return storyLikeRepository.save(StoryLike(user, story))
    }

    fun cancelLikeStory(user:User, story:Story){
        if(isExist(user,story).not()){
            throw NotFoundException(ResultCode.NOT_FOUND_STORYLIKE)
        }
        storyLikeRepository.deleteByUserAndStory(user,story)
    }

    fun isExistStoryLike(user:User, story: Story): Boolean{
        return isExist(user, story)
    }

    fun deleteStory(story: Story){
        story.status = StoryStatus.DELETED
    }

    fun getActiveComment(commentId:Long): Comment{
        val comment = commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        if(comment.status != CommentStatus.ACTIVE){
            throw NotFoundException(ResultCode.NOT_FOUND_COMMENT)
        }

        return comment
    }

    fun getComments(story:Story, pageable: Pageable): Page<Comment>{
        return commentRepository.findByStory(story, pageable)
    }

    fun getActiveComments(story:Story, pageable: Pageable): Page<Comment>{
        return commentRepository.findActiveCommentsByStory(story, pageable)
    }

    fun createComment(user:User, story:Story, contents:String):Comment{
        val comment = Comment(story, user, contents, CommentStatus.ACTIVE)
        return commentRepository.save(comment)
    }

    fun deleteComment(comment:Comment){
        comment.status = CommentStatus.DELETED
    }

    fun likeComment(user: User, commentId: Long) {
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        commentLikeRepository.save(CommentLike(user, comment))
    }

    fun dislikeComment(user:User, commentId:Long){
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        commentDislikeRepository.save(CommentDislike(user, comment))
    }

    fun getReplies(commentId:Long, pageable: Pageable):Page<CommentReply>{
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        return replyRepository.findByComment(comment, pageable)
    }

    fun getActiveReplies(commentId:Long, pageable: Pageable): Page<CommentReply>{
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        return replyRepository.findActiveByComment(comment, pageable)
    }

    fun createReply(user:User, commentId:Long, contents: String): CommentReply{
        val comment =
            commentRepository.findById(commentId).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_COMMENT) }
        return replyRepository.save(CommentReply(comment, user, contents, CommentStatus.ACTIVE))
    }

    fun getPerfumeIdsByMostStories(from: LocalDate, to: LocalDate, pageable: Pageable): List<Long> {
        return storyRepository.findPerfumeIdsByMostStories(from, to, pageable)
    }

    fun getPerfumeIdsByMostStories(pageable: Pageable): List<Long> {
        return storyRepository.findPerfumeIdsByMostStories(pageable)
    }

    /**
     *
     * private
     */
    private fun isExist(user:User, story: Story): Boolean{
        return storyLikeRepository.existsByUserAndStory(user, story)
    }

    fun getActiveStoryCountByPerfume(perfume: Perfume): Long {
        return storyRepository.countActiveStoryByPerfume(perfume)
    }


}


data class StorySaveCommand(
    val user: User,
    val perfume: Perfume,
    val image: Image,
    val viewType: StoryViewType
)
