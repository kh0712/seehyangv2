package kr.mashup.seehyangbusiness.port

import kr.mashup.seehyangcore.vo.StorageType
import java.io.InputStream

interface StorageService {

    fun save(inputStream: InputStream, extension:String): StorageInfo
}

data class StorageInfo(
    val url:String,
    val storageType: StorageType
)