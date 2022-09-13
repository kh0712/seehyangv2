package kr.mashup.seehyangweb.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SpringDocConfig {

    @Bean
    fun publicApi(): OpenAPI {
        val req = SecurityRequirement().addList("bearerAuth")
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .`in`(SecurityScheme.In.HEADER)
                            .name("Authorization")
                    )
            ).security(listOf(req))
    }
}