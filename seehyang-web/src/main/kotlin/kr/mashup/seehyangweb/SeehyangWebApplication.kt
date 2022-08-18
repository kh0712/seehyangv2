package kr.mashup.seehyangweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["kr.mashup.seehyangbusiness","kr.mashup.seehyangcore","kr.mashup.seehyangrds","kr.mashup.seehyangweb"])
@EnableJpaRepositories(basePackages = ["kr.mashup.seehyangrds"])
@EntityScan(basePackages = ["kr.mashup.seehyangrds"])
class SeehyangWebApplication

fun main(args: Array<String>) {
    runApplication<SeehyangWebApplication>(*args)
}
