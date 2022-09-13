package kr.mashup.seehyangweb.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


@EnableAsync
@Configuration
class AsyncConfig {

    @Bean(name = ["threadPoolTaskExecutor"])
    fun threadPoolTaskExecutor(): Executor? {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 3
        taskExecutor.maxPoolSize = 30
        //taskExecutor.queueCapacity = 100
        taskExecutor.setThreadNamePrefix("Executor-")
        return taskExecutor
    }
}