package kr.mashup.seehyangadmin.controller

import kr.mashup.seehyangadmin.SeehyangAdminResponse
import kr.mashup.seehyangadmin.common.AdminV1
import kr.mashup.seehyangadmin.service.AdminPerfumeService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@AdminV1
class AdminPerfumeController(
    private val adminPerfumeService: AdminPerfumeService
) {

//    @PostMapping("perfume")
//    fun createPerfume(
//        @Valid @RequestBody createPerfumeRequest: CreatePerfumeRequest
//    ):SeehyangAdminResponse<Long>{
//        val perfumeId : Long =  adminPerfumeService.createPerfume()
//        return SeehyangAdminResponse.success(perfumeId)
//    }
}

//data class CreatePerfumeRequest()