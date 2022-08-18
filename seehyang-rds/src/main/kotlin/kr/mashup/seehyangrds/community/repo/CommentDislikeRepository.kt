package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.CommentDislike
import org.springframework.data.jpa.repository.JpaRepository

interface CommentDislikeRepository: JpaRepository<CommentDislike, Long> {
}