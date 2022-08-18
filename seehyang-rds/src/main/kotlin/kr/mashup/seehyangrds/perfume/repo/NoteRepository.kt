package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.Note
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<Note, Long> {
}