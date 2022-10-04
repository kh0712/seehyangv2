package kr.mashup.seehyangredis.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

    @Bean
    fun redissonClient(
        @Value("\${redis.host}") redisHost: String,
        @Value("\${redis.port}") redisPort: String,
    ): RedissonClient = Redisson.create(Config().also { config ->
        config.codec = StringCodec("UTF-8")
        config.useSingleServer().apply {
            address = "redis://${redisHost}:${redisPort}"
            connectionMinimumIdleSize = 10
            connectionPoolSize = 50
            connectTimeout = 1000 * 5
            timeout = 1000 * 5
            idleConnectionTimeout = 1000 * 3
        }
    })


}