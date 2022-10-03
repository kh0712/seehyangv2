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
import java.time.LocalDateTime

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

    // TODO: 동시성 제어에 대한 고민
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

    fun getActiveStoryByIds(ids: List<Long>): List<Story> {
        return storyRepository
            .findAllById(ids)
            .filter { it.status == StoryStatus.ACTIVE }
            .toList()
    }

    fun getAnyStoryByIdOrThrow(id: Long):Story{
        return storyRepository.findById(id).orElseThrow { NotFoundException(ResultCode.NOT_FOUND_STORY) }
    }

    fun getLikeCount(story: Story): Long {
        return storyLikeRepository.countByStory(story)
    }


    fun getPublicStories(pageable: Pageable):List<Story>{
        return storyRepository.findPublicStories(pageable)
    }

    fun getPublicStoriesOrderByLike(pageable: Pageable): List<Story> {
        return storyRepository.findPublicStoriesOrderByLike(pageable)
    }


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


    fun likeOrCancelStory(user: User, story: Story): Boolean {
        if(isExistLike(user, story)){
            storyLikeRepository.deleteByUserAndStory(user,story)
            return false
        }
        storyLikeRepository.save(StoryLike(user, story))
        return true
    }

    fun isExistStoryLike(user:User, story: Story): Boolean{
        return isExistLike(user, story)
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

    fun getAnyComments(story:Story, pageable: Pageable): Page<Comment>{
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

    fun likeComment(user: User, comment:Comment) {

        commentLikeRepository.save(CommentLike(user, comment))
    }

    fun dislikeComment(user:User, comment:Comment){

        commentDislikeRepository.save(CommentDislike(user, comment))
    }

    fun getAnyReplies(comment:Comment, pageable: Pageable):Page<CommentReply>{

        return replyRepository.findByComment(comment, pageable)
    }

    fun getActiveReplies(comment:Comment, pageable: Pageable): Page<CommentReply>{

        return replyRepository.findActiveByComment(comment, pageable)
    }

    fun createReply(user:User, comment:Comment, contents: String): CommentReply{

        return replyRepository.save(CommentReply(comment, user, contents, CommentStatus.ACTIVE))
    }

    fun getPerfumeIdsByMostStories(from: LocalDateTime, to: LocalDateTime, pageable: Pageable): List<Long> {
        return storyRepository.findPerfumeIdsByMostStories(from, to, pageable)
    }

    fun getPerfumeIdsByMostStories(pageable: Pageable): List<Long> {
        return storyRepository.findPerfumeIdsByMostStories(pageable)
    }

    /**
     *
     * private
     */
    private fun isExistLike(user:User, story: Story): Boolean{
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
