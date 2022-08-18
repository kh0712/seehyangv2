package kr.mashup.seehyangrds.perfume.repo

import kr.mashup.seehyangrds.perfume.entity.Perfume
import kr.mashup.seehyangrds.perfume.entity.PerfumeLike
import kr.mashup.seehyangrds.user.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface PerfumeLikeRepository : JpaRepository<PerfumeLike, Long> {

    @Query("select pl " +
                   "from PerfumeLike pl " +
                   "join fetch pl.user u " +
                   "join fetch pl.perfume p " +
                   "where u =:user and p =:perfume")
    fun findByUserAndPerfume(@Param("user") user: User, @Param("perfume") perfume: Perfume) : PerfumeLike?

    fun existsByPerfumeAndUser(perfume: Perfume, user: User):Boolean

    fun countByPerfume(perfume: Perfume):Long
}