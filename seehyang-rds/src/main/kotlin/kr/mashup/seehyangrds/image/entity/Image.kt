package kr.mashup.seehyangrds.image.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangcore.vo.ImageStatus
import kr.mashup.seehyangcore.vo.StorageType
import kr.mashup.seehyangrds.user.entity.User
import javax.persistence.*

@Entity
class Image (
    uploader: User,
    url : String,
    status: ImageStatus,
    type: StorageType
) : BaseEntity(){

    @ManyToOne
    val uploader:User = uploader

    val url : String = url

    @Enumerated(EnumType.STRING)
    var status: ImageStatus =status
        internal set

    @Enumerated(EnumType.STRING)
    val type: StorageType = type
}