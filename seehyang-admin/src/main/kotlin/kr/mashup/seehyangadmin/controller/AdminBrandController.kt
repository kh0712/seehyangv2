package kr.mashup.seehyangadmin.controller

import kr.mashup.seehyangadmin.common.AdminV1
import kr.mashup.seehyangadmin.service.AdminBrandService
import org.springframework.web.bind.annotation.RestController

@AdminV1
class AdminBrandController(
    private val adminBrandService: AdminBrandService,
) {
    // Brand CRUD
}