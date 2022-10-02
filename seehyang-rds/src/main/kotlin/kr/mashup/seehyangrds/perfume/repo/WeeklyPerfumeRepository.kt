package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.SteadyPerfume
import kr.mashup.seehyangrds.perfume.entity.WeeklyPerfume
import org.springframework.data.jpa.repository.JpaRepository

interface WeeklyPerfumeRepository: JpaRepository<WeeklyPerfume, Long> {
}