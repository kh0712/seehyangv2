package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.Comment
import kr.mashup.seehyangrds.community.entity.Story
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface CommentRepository: JpaRepository<Comment, Long> {

    fun findByStory(story: Story, pageable: Pageable): Page<Comment>

    @Query("select c from Comment c where c.status = 'ACTIVE' and c.story = story")
    fun findActiveCommentsByStory(@Param("story") story: Story, pageable: Pageable): Page<Comment>
}