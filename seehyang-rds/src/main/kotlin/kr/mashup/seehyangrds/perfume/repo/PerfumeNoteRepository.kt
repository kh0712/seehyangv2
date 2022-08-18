package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.PerfumeNote
import org.springframework.data.jpa.repository.JpaRepository

interface PerfumeNoteRepository : JpaRepository<PerfumeNote, Long> {
}