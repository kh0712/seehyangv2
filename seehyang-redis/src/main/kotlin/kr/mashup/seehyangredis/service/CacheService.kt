package kr.mashup.seehyangredis.service

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.redisson.api.RLock
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.naming.ServiceUnavailableException

@Component
class CacheService(
    private val redissonClient: RedissonClient
) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    private val userCache = redissonClient.getMapCache<String,String>("user")

    @CircuitBreaker(name = "redisCache", fallbackMethod = "getCacheMiss")
    fun getUser(key:String):String? = userCache[key]

    @Retry(name="redisCache", fallbackMethod = "cacheFailLog")
    fun putUser(key: String, value:String, expMills:Long) {
        userCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val perfumeCache = redissonClient.getMapCache<String,String>("perfume")

    @CircuitBreaker(name = "redisCache", fallbackMethod = "getCacheMiss")
    fun getPerfume(key:String):String? = perfumeCache[key]

    @Retry(name="redisCache", fallbackMethod = "cacheFailLog")
    fun putPerfume(key: String, value:String, expMills:Long) {
        perfumeCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val storyCache =  redissonClient.getMapCache<String,String>("story")

    @CircuitBreaker(name = "redisCache", fallbackMethod = "getCacheMiss")
    fun getStory(key:String):String? = storyCache[key]
    @Retry(name="redisCache", fallbackMethod = "cacheFailLog")
    fun putStory(key: String, value:String, expMills:Long) {
        storyCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    private val storyLikeCache = redissonClient.getMapCache<String, String>("storyLike")

    @CircuitBreaker(name = "redisCache", fallbackMethod = "getCacheMiss")
    fun getStoryLike(key:String):String? = storyLikeCache[key]

    @Retry(name="redisCache", fallbackMethod = "cacheFailLog")
    fun putStoryLike(key: String, value:String, expMills:Long) {
        storyLikeCache.fastPut(key, value, expMills, TimeUnit.MILLISECONDS)
    }

    @CircuitBreaker(name = "redisCache")
    fun getLock(lockName: String, waitMs:Long,leaseMs:Long){
        val lock = redissonClient.getLock(lockName)
        if(!lock.tryLock(waitMs, leaseMs, TimeUnit.MILLISECONDS)){
            throw ServiceUnavailableException()
        }
    }
    @CircuitBreaker(name = "redisCache")
    fun unlock(lockName: String){
        val lock = redissonClient.getLock(lockName)
        lock.unlock()
    }

    private fun getCacheMiss():String? = null
    private fun cacheFailLog() = logger.error("cache put fail")
}