package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.SteadyPerfume
import org.springframework.data.jpa.repository.JpaRepository

interface SteadyPerfumeRepository: JpaRepository<SteadyPerfume, Long> {
}