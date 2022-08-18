package kr.mashup.seehyangweb.config

import kr.mashup.seehyangweb.config.resolver.CommentSortRequestArgumentResolver
import kr.mashup.seehyangweb.config.resolver.StorySortRequestArgumentResolver
import kr.mashup.seehyangweb.config.resolver.UserArgumentResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(
    private val userArgumentResolver: UserArgumentResolver,
    private val storySortRequestArgumentResolver: StorySortRequestArgumentResolver,
    private val commentSortRequestArgumentResolver: CommentSortRequestArgumentResolver
) : WebMvcConfigurer{

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userArgumentResolver)
        resolvers.add(storySortRequestArgumentResolver)
        resolvers.add(commentSortRequestArgumentResolver)
        super.addArgumentResolvers(resolvers)
    }
}