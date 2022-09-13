package kr.mashup.seehyangrds.home

import kr.mashup.seehyangrds.common.BaseEntity
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class HotStory(
    val storyId: Long,
    val likeCount: Long,
    val day: LocalDate
) : BaseEntity()