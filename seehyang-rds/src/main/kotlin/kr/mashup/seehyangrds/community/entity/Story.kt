package kr.mashup.seehyangrds.community.entity

import kr.mashup.seehyangcore.vo.StoryViewType
import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.image.entity.Image
import kr.mashup.seehyangrds.perfume.entity.Perfume
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.*

@Entity
class Story(
    user : User,
    perfume : Perfume,
    image: Image,
    viewType: StoryViewType
) : BaseEntity(){

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user : User = user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    val perfume : Perfume = perfume

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    val image: Image = image

    @Enumerated(EnumType.STRING)
    var viewType: StoryViewType = viewType
        internal set

    @Enumerated(EnumType.STRING)
    var status:StoryStatus = StoryStatus.ACTIVE
        internal set
}