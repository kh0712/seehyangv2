package kr.mashup.seehyangrds.home

import kr.mashup.seehyangrds.common.TransactionalService
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import kotlin.streams.toList

@TransactionalService
class HotStoryDomain(
    private val hotStoryRepository: HotStoryRepository
) {

    fun getHotStoryIds(base: LocalDate, pageable: Pageable): List<Long>{
        return hotStoryRepository
            .findByDay(base, pageable)
            .stream()
            .map { it->it.storyId }
            .toList()
    }
}