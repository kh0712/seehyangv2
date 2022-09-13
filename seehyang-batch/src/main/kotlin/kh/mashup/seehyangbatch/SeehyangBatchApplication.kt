package kh.mashup.seehyangbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@EnableBatchProcessing
@ComponentScan(basePackages = ["kr.mashup.seehyangcore","kr.mashup.seehyangrds","kh.mashup.seehyangbatch"])
@SpringBootApplication
class SeehyangBatchApplication

fun main(args: Array<String>) {
    runApplication<SeehyangBatchApplication>(*args)
}
