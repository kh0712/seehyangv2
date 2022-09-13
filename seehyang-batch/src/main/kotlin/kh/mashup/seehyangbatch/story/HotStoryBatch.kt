package kh.mashup.seehyangbatch.story

import kr.mashup.seehyangrds.community.repo.StoryRepository
import kr.mashup.seehyangrds.home.HotStory
import kr.mashup.seehyangrds.home.HotStoryRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import kotlin.streams.toList


@Configuration
class HotStoryBatch(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val hotStoryRepository: HotStoryRepository,
    private val storyRepository: StoryRepository
) {

    @Bean
    fun hotStoryJob():Job{
        // job parameter --job.name=hotStoryJob requestDate=2022/08/14
        return jobBuilderFactory
            .get("hotStoryJob")
            .start(hotStoryStep(null))
            .build()
    }


    @JobScope
    @Bean
    fun hotStoryStep(
        @Value("#{jobParameters[requestDate]}") requestDate:Date?
    ):Step{
        return stepBuilderFactory
            .get("hotStoryStep")
            .tasklet { contribution, chunkContext ->
                return@tasklet if(requestDate != null){
                    val targetDate = requestDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    val startDateTime = LocalDateTime.of(targetDate, LocalTime.MIN)
                    val endDateTime  = LocalDateTime.of(targetDate, LocalTime.MAX)
                    println(startDateTime)
                    println(endDateTime)
                    val hotStorys = storyRepository
                        .getHotStoryDto(startDateTime,endDateTime)
                        .stream()
                        .map { HotStory(it.storyId, it.count, targetDate) }
                        .toList()
                    hotStoryRepository.saveAll(hotStorys)
                    RepeatStatus.FINISHED
                }else{
                    throw IllegalArgumentException("request Date must not be null")
                }
             }
            .build()
    }


}
