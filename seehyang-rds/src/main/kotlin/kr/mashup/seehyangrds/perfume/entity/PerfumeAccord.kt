package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import javax.persistence.*

@Entity
class PerfumeAccord(

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accord_id")
    val accord: Accord

) : BaseEntity()