package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangcore.vo.PerfumeType
import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangrds.image.entity.Image
import javax.persistence.*

@Entity
class Perfume(
    name: String,
    koreanName: String,
    type: PerfumeType,
    gender: Gender,
    thumbnail: Image,
    brand: Brand,
    accords: MutableList<PerfumeAccord> = mutableListOf(),
    notes: MutableList<PerfumeNote> = mutableListOf()
) : BaseEntity() {

    var name: String = name
        internal set

    var koreanName: String = koreanName
        internal set

    @Enumerated(EnumType.STRING)
    var type: PerfumeType = type
        internal set

    @Enumerated(EnumType.STRING)
    var gender: Gender = gender
        internal set

    @OneToOne(fetch = FetchType.LAZY)
    var thumbnail: Image = thumbnail
        internal set

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    var brand: Brand = brand
        internal set

    @OneToMany(mappedBy = "perfume")
    val accords: MutableList<PerfumeAccord> = mutableListOf()

    @OneToMany(mappedBy = "perfume")
    val notes: MutableList<PerfumeNote> = mutableListOf()
}