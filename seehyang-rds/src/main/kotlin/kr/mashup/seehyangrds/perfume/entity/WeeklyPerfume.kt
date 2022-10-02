package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import java.time.LocalDate
import javax.persistence.Entity

@Entity
class WeeklyPerfume(
    val perfumeId: Long,
    val count: Int,
    val baseDate: LocalDate
) :BaseEntity(){
}