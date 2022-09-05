package kr.mashup.seehyangweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan(basePackages = ["kr.mashup.seehyangredis","kr.mashup.seehyangbusiness","kr.mashup.seehyangcore","kr.mashup.seehyangrds","kr.mashup.seehyangweb"])
class SeehyangWebApplication

fun main(args: Array<String>) {
    runApplication<SeehyangWebApplication>(*args)
}
