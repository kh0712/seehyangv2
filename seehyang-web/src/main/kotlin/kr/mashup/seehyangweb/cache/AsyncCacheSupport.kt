package kr.mashup.seehyangweb.cache

import kr.mashup.seehyangbusiness.business.PerfumeInfo
import kr.mashup.seehyangbusiness.business.StoryInfo
import kr.mashup.seehyangbusiness.business.UserInfo
import kr.mashup.seehyangredis.service.CacheService
import kr.mashup.seehyangweb.common.toJson
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AsyncCacheSupport(
    private val cacheService: CacheService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Async(value = "threadPoolTaskExecutor")
    fun putUserAsync(userInfo: UserInfo) {
        log.info("user cache put user id ${userInfo.id}")
        try {
            cacheService.putUser(key = userInfo.id.toString(), userInfo.toJson(), 1000 * 60 * 5)
        } catch (e: Exception) {
            log.error("cache error : ${e.message}")
        }
    }

    @Async(value = "threadPoolTaskExecutor")
    fun putPerfumeAsync(perfumeInfo: PerfumeInfo) {
        log.info("perfume cache put perfume id ${perfumeInfo.id}")
        try {
            cacheService.putPerfume(key = perfumeInfo.id.toString(), perfumeInfo.toJson(), 1000 * 60 * 5)
        } catch (e: Exception) {
            log.error("cache error : ${e.message}")
        }
    }

    @Async(value = "threadPoolTaskExecutor")
    fun putStoryAsync(storyInfo: StoryInfo) {
        log.info("story cache put story id ${storyInfo.id}")
        try {
            cacheService.putStory(key = storyInfo.id.toString(), storyInfo.toJson(), 1000 * 60 * 5)
        } catch (e: Exception) {
            log.error("cache error : ${e.message}")
        }
    }

    @Async(value = "threadPoolTaskExecutor")
    fun putStoryLikeAsync(storyId: Long, storyLike: Long) {
        log.info("storyLike cache put story id ${storyId}")
        try {
            cacheService.putStoryLike(key = storyId.toString(), storyLike.toString(), 1000 * 60 * 5)
        } catch (e: Exception) {
            log.error("cache error : ${e.message}")
        }
    }

}