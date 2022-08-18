package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import javax.persistence.*

@Entity
class Brand(
    var name: String,
    var koreanName: String,
) : BaseEntity(){

}