package kr.mashup.seehyangrds.common

import kr.mashup.seehyangcore.exception.InternalServerException
import kr.mashup.seehyangcore.exception.ResultCode
import org.springframework.data.domain.Pageable


fun checkIsPaged(pageable: Pageable){
    if(pageable.isUnpaged){
        throw InternalServerException(ResultCode.UNPAGED_ERROR)
    }
}