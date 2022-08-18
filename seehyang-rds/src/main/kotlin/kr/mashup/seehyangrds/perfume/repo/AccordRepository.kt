package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.Accord
import org.springframework.data.jpa.repository.JpaRepository

interface AccordRepository : JpaRepository<Accord, Long> {
}