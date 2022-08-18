package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import javax.persistence.*

@Entity
class Note(
    var name: String,
    var koreanName: String
) : BaseEntity()