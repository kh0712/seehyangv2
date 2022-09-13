package kr.mashup.seehyangrds.home

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface HotStoryRepository : JpaRepository<HotStory, Long> {

    fun findByDay(date: LocalDate, pageable: Pageable):List<HotStory>
}