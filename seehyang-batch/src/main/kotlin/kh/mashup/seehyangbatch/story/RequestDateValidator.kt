package kh.mashup.seehyangbatch.story

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.format.datetime.DateFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.DateTimeParseException

class RequestDateValidator(
    private val requiredKey: List<String>
) : JobParametersValidator {


    override fun validate(parameters: JobParameters?) {
        if(parameters ===  null){
            throw JobParametersInvalidException("The JobParameters can not be null")
        }
        val keys: Set<String> = parameters.parameters.keys
        for(key in requiredKey){
            val date = parameters.getString(key) ?: throw JobParametersInvalidException("${key} can not be null")
            try{
                val formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd")
                LocalDate.parse(key, formatter)
            }catch (e : DateTimeParseException){
                throw JobParametersInvalidException("${key} is not 'yyyy-mm-dd' format")
            }
        }
    }
}