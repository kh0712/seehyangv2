package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.Comment
import kr.mashup.seehyangrds.community.entity.CommentReply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CommentReplyRepository: JpaRepository<CommentReply, Long> {

    fun findByComment(comment: Comment, pageable: Pageable): Page<CommentReply>

    @Query("select cr from CommentReply cr where cr.status = 'ACTIVE'")
    fun findActiveByComment(comment: Comment, pageable: Pageable): Page<CommentReply>
}