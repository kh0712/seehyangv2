package kr.mashup.seehyangweb.config.converter

import kr.mashup.seehyangcore.vo.StoryViewType
import org.springframework.core.convert.converter.Converter

class StoryViewTypeConverter: Converter<String, StoryViewType> {

    override fun convert(source: String): StoryViewType? {
        return StoryViewType.valueOf(source)
    }

    override fun <U : Any?> andThen(after: Converter<in StoryViewType, out U>): Converter<String, U> {
        return super.andThen(after)
    }


}