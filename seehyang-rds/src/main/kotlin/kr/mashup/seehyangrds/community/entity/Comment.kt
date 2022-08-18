package kr.mashup.seehyangrds.community.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.*

@Entity
class Comment(
    story: Story,
    user : User,
    contents: String,
    status: CommentStatus

) : BaseEntity(){
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    val story: Story = story

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User = user

    var contents: String = contents
        internal set

    @Enumerated(EnumType.STRING)
    var status: CommentStatus = status
        internal set
}