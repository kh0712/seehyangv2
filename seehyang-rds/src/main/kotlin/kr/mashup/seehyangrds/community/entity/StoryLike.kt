package kr.mashup.seehyangrds.community.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.*

@Entity
//@Table(uniqueConstraints = )
class StoryLike(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    val story: Story

) : BaseEntity()
