package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.CommentLike
import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
}