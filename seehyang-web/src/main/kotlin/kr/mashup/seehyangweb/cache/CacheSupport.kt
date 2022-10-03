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
    private val cacheService: CacheService,
    private val asyncCacheSupport: AsyncCacheSupport
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val storyLikeLockPrefix = "STORY_LIKE_LOCK"

    fun getUser(userId: Long): UserInfo? {
        val userCache: String? = cacheService.getUser(key = userId.toString())

        if (userCache != null) {
            log.info("user cache hit user id ${userId}")
        } else {
            log.info("user cache miss user id ${userId}")
        }

        return userCache?.let { DEFAULT_OBJECT_MAPPER.readValue(it, UserInfo::class.java) }
    }


    fun putUserAsync(userInfo: UserInfo) {
        asyncCacheSupport.putUserAsync(userInfo)
    }

    fun getPerfume(perfumeId: Long): PerfumeInfo? {
        var perfumeCache: String? = cacheService.getPerfume(key = perfumeId.toString())

        if (perfumeCache != null) {
            log.info("perfume cache hit perfume id ${perfumeId}")
        } else {
            log.info("perfume cache miss perfume id ${perfumeId}")
        }

        return perfumeCache?.let { DEFAULT_OBJECT_MAPPER.readValue(it, PerfumeInfo::class.java) }
    }


    fun putPerfumeAsync(perfumeInfo: PerfumeInfo) {
        asyncCacheSupport.putPerfumeAsync(perfumeInfo)
    }

    fun getStory(storyId: Long): StoryInfo? {
        var storyCache: String? = cacheService.getStory(key = storyId.toString())

        if (storyCache != null) {
            log.info("story cache hit story id ${storyId}")
        } else {
            log.info("story cache miss story id ${storyId}")
        }

        return storyCache?.let { DEFAULT_OBJECT_MAPPER.readValue(it, StoryInfo::class.java) }
    }


    fun putStoryAsync(storyInfo: StoryInfo) {
        asyncCacheSupport.putStoryAsync(storyInfo)
    }

    fun getStoryLikeCount(storyId: Long): Long? {
        var storyLikeCache: String? = cacheService.getStoryLike(key = storyId.toString())

        if (storyLikeCache != null) {
            log.info("storyLike cache hit story id ${storyId}")
        } else {
            log.info("storyLike cache miss story id ${storyId}")
        }

        return storyLikeCache?.let { DEFAULT_OBJECT_MAPPER.readValue(it, Long::class.java) }
    }


    fun putStoryLikeAsync(storyId: Long, storyLike: Long) {
        asyncCacheSupport.putStoryLikeAsync(storyId, storyLike)
    }

    fun getStoryLikeLock(storyId: Long, userId: Long, waitMs:Long, leaseMs:Long){
        cacheService.getLock(storyLikeLockPrefix+":${storyId}:${userId}",waitMs, leaseMs)
    }

    fun unlockStoryLike(storyId: Long, userId: Long){
        cacheService.unlock(storyLikeLockPrefix+":${storyId}:${userId}")
    }


}