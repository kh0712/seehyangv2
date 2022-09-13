package kr.mashup.seehyangweb.api

import io.swagger.v3.oas.annotations.Parameter
import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.ImageUploadFacadeService
import kr.mashup.seehyangweb.facade.ImageUploadResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile


@ApiV1
class ImageController(
    private val imageFacadeService: ImageUploadFacadeService
) {

    @GetMapping("/image/{imageId}", produces = [MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE])
    fun download(
        @PathVariable imageId: Long,
    ): ByteArray {

        return imageFacadeService.download(imageId).image
    }

    @PostMapping("/image")
    fun upload(
        @Parameter(hidden = true) userAuth: UserAuth,
        @RequestParam(required = false) image: MultipartFile
    ): SeehyangResponse<ImageUploadResponse> {

        val imageUploadResponse = imageFacadeService.upload(userAuth, image)
        return SeehyangResponse.success(imageUploadResponse)
    }
}