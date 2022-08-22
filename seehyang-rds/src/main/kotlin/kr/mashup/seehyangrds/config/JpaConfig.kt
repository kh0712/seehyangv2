package kr.mashup.seehyangrds.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["kr.mashup.seehyangrds"])
@EntityScan(basePackages = ["kr.mashup.seehyangrds"])
class JpaConfig {
}