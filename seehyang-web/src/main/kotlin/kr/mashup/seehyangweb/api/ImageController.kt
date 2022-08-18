package kr.mashup.seehyangweb.api

import kr.mashup.seehyangweb.auth.UserAuth
import kr.mashup.seehyangweb.common.ApiV1
import kr.mashup.seehyangweb.common.SeehyangResponse
import kr.mashup.seehyangweb.facade.ImageDownloadResponse
import kr.mashup.seehyangweb.facade.ImageUploadFacadeService
import kr.mashup.seehyangweb.facade.ImageUploadResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile


@ApiV1
class ImageController(
    private val imageFacadeService: ImageUploadFacadeService
) {

    @GetMapping("/image/{imageId}")
    fun download(
        @PathVariable imageId: Long,
    ): SeehyangResponse<ByteArray> {

        return SeehyangResponse.success(imageFacadeService.download(imageId).image)
    }

    @PostMapping("/image")
    fun upload(
        userAuth: UserAuth,
        @RequestParam multipartFile: MultipartFile
    ): SeehyangResponse<ImageUploadResponse> {
        val imageUploadResponse = imageFacadeService.upload(userAuth, multipartFile)
        return SeehyangResponse.success(imageUploadResponse)
    }
}