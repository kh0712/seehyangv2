package kr.mashup.seehyangweb.cache

import kr.mashup.seehyangredis.service.CacheService
import org.springframework.stereotype.Component

@Component
class PerfumeCache (
    private val cacheService: CacheService){
}