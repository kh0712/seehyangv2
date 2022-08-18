package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.*

@Entity
class PerfumeLike(
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    val user : User,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume
) : BaseEntity(){

}