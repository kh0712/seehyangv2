package kr.mashup.seehyangadmin.service

import kr.mashup.seehyangrds.common.TransactionalService
import kr.mashup.seehyangrds.community.domain.StoryDomain

@TransactionalService
class AdminStoryService(
    private val storyDomain: StoryDomain
) {
}