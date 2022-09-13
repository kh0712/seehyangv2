package kr.mashup.seehyangweb.cache

import kr.mashup.seehyangbusiness.business.PerfumeInfo
import kr.mashup.seehyangbusiness.business.StoryInfo
import kr.mashup.seehyangbusiness.business.UserInfo
import kr.mashup.seehyangredis.service.CacheService
import kr.mashup.seehyangweb.common.DEFAULT_OBJECT_MAPPER
import kr.mashup.seehyangweb.common.toJson
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class CacheSupport(
    private val cacheService: CacheService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun getUser(userId: Long): UserInfo? {
        var userCache:String?
        try{
            userCache = cacheService.getUser(key = userId.toString())
        }catch (e: Exception){
            userCache = null
            log.error("cache error : ${e.message}")
        }
        if(userCache!=null){
            log.info("user cache hit user id ${userId}")
        }else{
            log.info("user cache miss user id ${userId}")
        }

        return userCache?.let{DEFAULT_OBJECT_MAPPER.readValue(it, UserInfo::class.java)}
    }


    @Async(value = "threadPoolTaskExecutor")
    fun putUserAsync(userInfo: UserInfo) {
        log.info("user cache put user id ${userInfo.id}")
        try {
            cacheService.putUser(key = userInfo.id.toString(), userInfo.toJson(), 1000 * 60 * 5)
        }catch (e: Exception){
            log.error("cache error : ${e.message}")
        }
    }

    fun getPerfume(perfumeId: Long): PerfumeInfo? {
        var perfumeCache: String?
        try {
            perfumeCache = cacheService.getPerfume(key = perfumeId.toString())
        }catch (e: Exception){
            log.error("cache error : ${e.message}")
            perfumeCache = null
        }
        if(perfumeCache!=null){
            log.info("perfume cache hit perfume id ${perfumeId}")
        }else{
            log.info("perfume cache miss perfume id ${perfumeId}")
        }

        return perfumeCache?.let{DEFAULT_OBJECT_MAPPER.readValue(it, PerfumeInfo::class.java)}
    }


    @Async(value = "threadPoolTaskExecutor")
    fun putPerfumeAsync(perfumeInfo: PerfumeInfo) {
        log.info("perfume cache put perfume id ${perfumeInfo.id}")
        try {
            cacheService.putPerfume(key = perfumeInfo.id.toString(), perfumeInfo.toJson(), 1000 * 60 * 5)
        }catch (e:Exception){
            log.error("cache error : ${e.message}")
        }
    }

    fun getStory(storyId: Long): StoryInfo? {
        var storyCache:String?
        try {
            storyCache = cacheService.getStory(key = storyId.toString())
        }catch (e: Exception){
            storyCache = null
            log.error("cache error : ${e.message}")
        }
        if(storyCache!=null){
            log.info("story cache hit story id ${storyId}")
        }else{
            log.info("story cache miss story id ${storyId}")
        }

        return storyCache?.let{DEFAULT_OBJECT_MAPPER.readValue(it, StoryInfo::class.java)}
    }


    @Async(value = "threadPoolTaskExecutor")
    fun putStoryAsync(storyInfo: StoryInfo) {
        log.info("story cache put story id ${storyInfo.id}")
        try {
            cacheService.putStory(key = storyInfo.id.toString(), storyInfo.toJson(), 1000 * 60 * 5)
        }catch (e: Exception){
            log.error("cache error : ${e.message}")
        }
    }

    fun getStoryLike(storyId: Long): Long? {
        var storyLikeCache:String?
        try{
            storyLikeCache = cacheService.getStoryLike(key = storyId.toString())
        }catch (e: Exception){
            storyLikeCache = null
            log.error("cache error : ${e.message}")
        }
        if(storyLikeCache!=null){
            log.info("storyLike cache hit story id ${storyId}")
        }else{
            log.info("storyLike cache miss story id ${storyId}")
        }

        return storyLikeCache?.let{DEFAULT_OBJECT_MAPPER.readValue(it, Long::class.java)}
    }


    @Async(value = "threadPoolTaskExecutor")
    fun putStoryLikeAsync(storyId:Long, storyLike: Long) {
        log.info("storyLike cache put story id ${storyId}")
        try {
            cacheService.putStoryLike(key = storyId.toString(), storyLike.toString(), 1000 * 60 * 5)
        }catch (e: Exception){
            log.error("cache error : ${e.message}")
        }
    }


}