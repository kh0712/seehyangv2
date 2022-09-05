package kr.mashup.seehyangredis.service

import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component

@Component
class CacheService (
    private val redissonClient: RedissonClient
){

    fun getMap(name: String) = redissonClient.getMap<String, String>(name)
}