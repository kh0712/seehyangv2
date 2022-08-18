package kr.mashup.seehyangrds.image.repository

import kr.mashup.seehyangrds.image.entity.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long> {

    fun findByUrl(url:String): Image?

    fun existsByUrl(url:String):Boolean
}