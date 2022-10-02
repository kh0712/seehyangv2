package kh.mashup.seehyangbatch.story

import kr.mashup.seehyangrds.community.entity.Story
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
@StepScope
class StoryPerfumeCountWriter : ItemWriter<Story>, StepExecutionListener{

    lateinit var jobExecutionContext: ExecutionContext

    override fun beforeStep(stepExecution: StepExecution) {
        jobExecutionContext = stepExecution.jobExecution.executionContext
        jobExecutionContext.put("count", HashMap<Long, Int>())
    }

    override fun afterStep(stepExecution: StepExecution): ExitStatus? {
        return stepExecution.exitStatus
    }

    override fun write(items: MutableList<out Story>) {
        val countMap = jobExecutionContext.get("count") as HashMap<Long, Int>
        for(story in items){
            countMap.put(story.perfume.id!!, countMap.getOrDefault(story.perfume.id!!, 0)+1)
        }
    }
}