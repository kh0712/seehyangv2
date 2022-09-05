package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.Perfume
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface PerfumeRepository : JpaRepository<Perfume, Long>{

    @EntityGraph(attributePaths = ["brand", "thumbnail"])
    override fun findById(id: Long): Optional<Perfume>

    @Query("select p from Perfume p where p.koreanName like :name or p.name like :name")
    fun findByNameOrKoreanName(@Param("name") name:String, pageable: Pageable): Page<Perfume>

}