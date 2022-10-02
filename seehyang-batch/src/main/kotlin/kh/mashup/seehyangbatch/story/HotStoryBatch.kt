package kh.mashup.seehyangbatch.story

import kr.mashup.seehyangcore.util.atEndOfDay
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.streams.toList


@Configuration
class HotStoryBatch(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val hotStoryRepository: HotStoryRepository,
    private val storyRepository: StoryRepository
) {

    @Bean
    fun hotStoryJob(): Job {
        // job parameter --job.name=hotStoryJob requestDate=2022/08/14
        return jobBuilderFactory
            .get("hotStoryJob")
            .start(hotStoryStep(null))
            .validator(RequestDateValidator(listOf("requestDate")))
            .build()
    }


    @JobScope
    @Bean
    fun hotStoryStep(
        @Value("#{jobParameters[requestDate]}") requestDate: String?
    ): Step {

        return stepBuilderFactory
            .get("hotStoryStep")
            .tasklet { contribution, chunkContext ->
                val requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val startDateTime = requestLocalDate.atStartOfDay()
                val endDateTime = requestLocalDate.atEndOfDay()
                val hotStorys = storyRepository
                    .getHotStoryDto(startDateTime, endDateTime)
                    .stream()
                    .map { HotStory(it.storyId, it.count, requestLocalDate) }
                    .toList()
                hotStoryRepository.saveAll(hotStorys)
                RepeatStatus.FINISHED
            }
            .build()
    }


}
