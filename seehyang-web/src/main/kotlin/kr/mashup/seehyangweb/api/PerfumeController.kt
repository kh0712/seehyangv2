package kr.mashup.seehyangweb.api

import io.swagger.v3.oas.annotations.Parameter
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.EmptyResponse
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.PerfumeBasicInfoResponse
import kr.mashup.seehyangweb.facade.PerfumeDetailInfoResponse
import kr.mashup.seehyangweb.facade.PerfumeFacadeService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@ApiV1
class PerfumeController(
    private val perfumeFacadeService: PerfumeFacadeService
) {

    @GetMapping("/perfume/{perfumeId}")
    fun getPerfumeDetail(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable perfumeId: Long,
    ) : SeehyangResponse<PerfumeDetailInfoResponse> {

        val perfume = perfumeFacadeService.getPerfume(userAuth,perfumeId)
        return SeehyangResponse.success(perfume)
    }

    @GetMapping("/perfume")
    fun getPerfumes(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PageableDefault pageable: Pageable,
    ) : SeehyangResponse<List<PerfumeBasicInfoResponse>> {

        val perfume = perfumeFacadeService.getPerfumes(userAuth,pageable)
        return SeehyangResponse.success(perfume)
    }

    @GetMapping("/perfume/search")
    fun getPerfumesByName(
        @Parameter(hidden = true) userAuth: UserAuth,
        @RequestParam(value = "name") name: String,
        @PageableDefault pageable: Pageable
    ): SeehyangResponse<List<PerfumeBasicInfoResponse>> {

        val perfumeInfos = perfumeFacadeService.getPerfumesByName(name,pageable)

        return SeehyangResponse.success(perfumeInfos)
    }

    @PostMapping("/perfume/{perfumeId}/like")
    fun likePerfume(
        @Parameter(hidden = true) userAuth: UserAuth,
        @PathVariable perfumeId: Long
    ): SeehyangResponse<EmptyResponse> {

        perfumeFacadeService.likePerfume(perfumeId, userAuth)
        return SeehyangResponse.success()
    }
}