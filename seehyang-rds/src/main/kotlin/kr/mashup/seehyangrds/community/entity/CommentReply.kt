package kr.mashup.seehyangrds.community.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.user.entity.User
import java.util.*
import javax.persistence.*

@Entity
class CommentReply(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    val comment: Comment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    val contents: String,

    @Enumerated(EnumType.STRING)
    val status: CommentStatus
) : BaseEntity()