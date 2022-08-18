package kr.mashup.seehyangweb.api

import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.PerfumeFacadeService
import kr.mashup.seehyangweb.facade.PerfumeInfoResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import springfox.documentation.annotations.ApiIgnore

@ApiV1
class PerfumeController(
    private val perfumeFacadeService: PerfumeFacadeService
) {

    @GetMapping("/perfume/{perfumeId}")
    fun getPerfumeDetail(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable perfumeId: Long,
    ) : SeehyangResponse<PerfumeInfoResponse> {

        val perfume = perfumeFacadeService.getPerfume(perfumeId,userAuth)
        return SeehyangResponse.success(perfume)
    }

    @GetMapping("/perfume/list")
    fun getPerfumesByName(
        @ApiIgnore userAuth: UserAuth,
        @RequestParam(value = "name") name: String,
        @PageableDefault pageable: Pageable
    ): SeehyangResponse<List<PerfumeInfoResponse>> {

        val perfumeInfos = perfumeFacadeService.getPerfumesByName(name,pageable)

        return SeehyangResponse.success(perfumeInfos)
    }

    @PostMapping("/perfume/{perfumeId}/like")
    fun likePerfume(
        @ApiIgnore userAuth: UserAuth,
        @PathVariable perfumeId: Long
    ): SeehyangResponse<EmptyResponse> {

        perfumeFacadeService.likePerfume(perfumeId, userAuth)
        return SeehyangResponse.success()
    }
}