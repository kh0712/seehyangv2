package kh.mashup.seehyangbatch.story

import kr.mashup.seehyangrds.community.entity.Story
import kr.mashup.seehyangrds.perfume.entity.SteadyPerfume
import kr.mashup.seehyangrds.perfume.repo.SteadyPerfumeRepository
import org.springframework.batch.core.*
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
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
import java.util.concurrent.ConcurrentHashMap
import javax.persistence.EntityManagerFactory

@Configuration
class SteadyPerfumeBatch(
    private val entityManagerFactory: EntityManagerFactory,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val steadyPerfumeRepository: SteadyPerfumeRepository
) {
    private val CHUNK_SIZE = 1000

    @Bean
    fun steadyPerfumeJob(): Job {
        return jobBuilderFactory.get("steadyPerfumeJob")
            .start(steadyPerfumeCountStep())
            .next(steadyPerfumeWriteStep(null))
            .build()
    }

    @Bean
    @JobScope
    fun steadyPerfumeCountStep(): Step {

        return stepBuilderFactory.get("steadyPerfumeJob")
            .chunk<Story, Story>(CHUNK_SIZE)
            .reader(storyReader(null))
            .writer(StoryPerfumeCountWriter())
            .build()
    }

    @Bean
    @StepScope
    fun storyReader(@Value("#{jobParameters[requestDate]}") requestDate: String?): JpaPagingItemReader<Story> {
        val requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val startDateTime = LocalDateTime.of(requestLocalDate, LocalTime.MIN)
        val endDateTime = LocalDateTime.of(requestLocalDate, LocalTime.MAX)
        val param = mutableMapOf<String, Any>()
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
    fun steadyPerfumeWriteStep(@Value("#{jobParameters[requestDate]}") requestDate: String?) : Step{
        return stepBuilderFactory.get("steadyPerfumeWriteStep")
            .tasklet { contribution, chunkContext ->
                val requestLocalDate = LocalDate.parse(requestDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val jobExecutionContext = contribution.stepExecution.jobExecution.executionContext
                val countMap = jobExecutionContext.get("count") as HashMap<Long, Int>
                for(entry in countMap){
                    val steadyPerfume = SteadyPerfume(entry.key, entry.value, requestLocalDate)
                    println(entry.key.toString() + " : " + entry.value)
                    steadyPerfumeRepository!!.save(steadyPerfume)
                }

                return@tasklet RepeatStatus.FINISHED
            }
            .build()
    }

}

