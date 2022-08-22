package kr.mashup.seehyangadmin.service

import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.perfume.domain.PerfumeDomain

@TransactionalService
class AdminPerfumeService(
    private val perfumeDomain: PerfumeDomain,
) {
}