package kr.mashup.seehyangbusiness.business

import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.home.HotStoryDomain
import org.springframework.data.domain.Pageable
import java.time.LocalDate

@TransactionalService
class HomeService(
    private val hotStoryDomain: HotStoryDomain
) {

    fun getStoryIds(base:LocalDate, pageable: Pageable):List<Long>{
        return hotStoryDomain.getHotStoryIds(base, pageable)
    }
}