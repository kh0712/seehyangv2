package kh.mashup.seehyangbatch.story

import kr.mashup.seehyangrds.community.entity.Story
import kr.mashup.seehyangrds.perfume.entity.WeeklyPerfume
import kr.mashup.seehyangrds.perfume.repo.WeeklyPerfumeRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityManagerFactory

@Configuration
class WeeklyRankingBatch(
    private val entityManagerFactory: EntityManagerFactory,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val weeklyPerfumeRepository: WeeklyPerfumeRepository
) {
    private val CHUNK_SIZE = 1000

    @Bean
    fun weeklyRankingJob(): Job {
        return jobBuilderFactory.get("weeklyRankingJob")
            .start(weeklyRankingCountStep())
            .next(weeklyRankingWriteStep(null))
            .build()
    }

    @Bean
    @JobScope
    fun weeklyRankingCountStep(): Step {

        return stepBuilderFactory.get("steadyPerfumeJob")
            .chunk<Story, Story>(CHUNK_SIZE)
            .reader(weeklyRankingReader(null))
            .writer(WeeklyRankingCountWriter())
            .build()
    }

    @Bean
    @StepScope
    fun weeklyRankingReader(@Value("#{jobParameters[requestDate]}") requestDate: String?): JpaPagingItemReader<Story> {
        val param = mutableMapOf<String, Any>()
        val date = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val startDateTime = LocalDateTime.of(date, LocalTime.MIN).minusWeeks(1L)
        val endDateTime = LocalDateTime.of(date, LocalTime.MAX)
        param.put("startDateTime", startDateTime)
        param.put("endDateTime", endDateTime)
        return JpaPagingItemReaderBuilder<Story>()
            .queryString("select s from Story s where s.status = 'ACTIVE' and s.viewType = 'PUBLIC' and s.createdAt >= :startDateTime and s.createdAt <= :endDateTime")
            .pageSize(CHUNK_SIZE)
            .entityManagerFactory(entityManagerFactory)
            .parameterValues(param)
            .name("storyReader")
            .build()
    }


    @Bean
    @JobScope
    fun weeklyRankingWriteStep(@Value("#{jobParameters[requestDate]}") requestDate: String?) : Step {
        return stepBuilderFactory.get("steadyPerfumeWriteStep")
            .tasklet { contribution, chunkContext ->
                val requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val jobExecutionContext = contribution.stepExecution.jobExecution.executionContext
                val countMap = jobExecutionContext.get("count") as HashMap<Long, Int>
                for(entry in countMap){
                    val weeklyPerfume = WeeklyPerfume(entry.key, entry.value, requestLocalDate)
                    println(entry.key.toString() + " : " + entry.value)
                    weeklyPerfumeRepository!!.save(weeklyPerfume)
                }

                return@tasklet RepeatStatus.FINISHED
            }
            .build()
    }


}