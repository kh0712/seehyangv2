package kr.mashup.seehyangrds.community.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class CommentLike (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    val comment : Comment,
):BaseEntity()