package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import javax.persistence.*

@Entity
class Accord(
    val name: String,
    val koreanName: String
) : BaseEntity()