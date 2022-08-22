package kr.mashup.seehyangadmin.service

import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.perfume.repo.BrandRepository

@TransactionalService
class AdminBrandService(
    private val brandRepository: BrandRepository
) {
}