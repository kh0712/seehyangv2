package kr.mashup.seehyangadmin.controller

import kr.mashup.seehyangadmin.common.AdminV1
import kr.mashup.seehyangadmin.service.AdminStoryService
import org.springframework.web.bind.annotation.RestController

@AdminV1
class AdminStoryController(
    private val adminStoryService:AdminStoryService
) {
    // Story CRUD
}