package kr.mashup.seehyangredis.service

import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class CacheService(
    private val redissonClient: RedissonClient
) {

    private val userCache = redissonClient.getMapCache<String,String>("user")

    fun getUser(key:String):String? = userCache[key]
    fun putUser(key: String, value:String, expMills:Long) {
        userCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val perfumeCache = redissonClient.getMapCache<String,String>("perfume")

    fun getPerfume(key:String):String? = perfumeCache[key]
    fun putPerfume(key: String, value:String, expMills:Long) {
        perfumeCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val storyCache =  redissonClient.getMapCache<String,String>("story")

    fun getStory(key:String):String? = storyCache[key]
    fun putStory(key: String, value:String, expMills:Long) {
        storyCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val storyLikeCache = redissonClient.getMapCache<String, String>("storyLike")

    fun getStoryLike(key:String):String? = storyLikeCache[key]
    fun putStoryLike(key: String, value:String, expMills:Long) {
        storyLikeCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }
}