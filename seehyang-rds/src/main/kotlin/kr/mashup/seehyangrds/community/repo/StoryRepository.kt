package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.Story
import kr.mashup.seehyangrds.perfume.entity.Perfume
import kr.mashup.seehyangrds.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

interface StoryRepository : JpaRepository<Story, Long> {

    fun findByUser(user: User, pageable: Pageable): Page<Story>

    fun findByPerfume(perfume: Perfume, pageable: Pageable): Page<Story>

    @Query("select s from Story s where s.perfume = :perfume and (s.viewType = 'PUBLIC' or  s.user = :user) and s.viewType <> 'ONLYADMIN'")
    fun findAccessibleByUserAndPerfume(@Param("user") user: User, @Param("perfume") perfume: Perfume, pageable: Pageable): Page<Story>

    // TODO: 뭔가 이상한데
    @Query("select s from Story s join StoryLike sl on s = sl.story where s.perfume = :perfume and (s.viewType = 'PUBLIC' or  s.user = :user) and s.viewType <> 'ONLYADMIN' group by s order by count(sl)")
    fun findAccessibleByUserAndPerfumeOrderByLike(@Param("user") user: User, @Param("perfume") perfume: Perfume, pageable: Pageable): Page<Story>

    @Query("select s from Story s where s.viewType = 'PUBLIC'")
    fun findPublicStories(pageable: Pageable): List<Story>

    @Query("select p.id from Perfume p join Story s on p.id = s.perfume.id where s.createdAt >= :from and s.createdAt <= :to group by p order by count(s)")
    fun findPerfumeIdsByMostStories(@Param("from") from: LocalDate, @Param("to") to: LocalDate, pageable: Pageable): List<Long>

    @Query("select p.id from Perfume p join Story s on p.id = s.perfume.id group by p order by count(s)")
    fun findPerfumeIdsByMostStories(pageable: Pageable): List<Long>

    @Query("select count(s) from Perfume p join Story s on p.id = s.perfume.id where p = :perfume")
    fun countActiveStoryByPerfume(@Param("perfume") perfume: Perfume): Long

}