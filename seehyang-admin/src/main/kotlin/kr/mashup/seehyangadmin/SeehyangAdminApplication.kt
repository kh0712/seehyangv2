package kr.mashup.seehyangadmin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["kr.mashup.seehyangcore","kr.mashup.seehyangrds"])
class SeehyangAdminApplication

fun main(args: Array<String>) {
    runApplication<SeehyangAdminApplication>(*args)
}
