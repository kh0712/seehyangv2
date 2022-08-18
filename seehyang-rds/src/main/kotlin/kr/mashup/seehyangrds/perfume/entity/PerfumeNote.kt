package kr.mashup.seehyangrds.perfume.entity

import kr.mashup.seehyangrds.common.BaseEntity
import javax.persistence.*

@Entity
class PerfumeNote(

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfume_id")
    val perfume: Perfume,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "note_id")
    val note: Note,

    @Enumerated(EnumType.STRING)
    val type: NoteType
) : BaseEntity()