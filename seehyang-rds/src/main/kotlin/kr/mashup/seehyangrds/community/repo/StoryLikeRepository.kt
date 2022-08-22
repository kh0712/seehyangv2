package kr.mashup.seehyangrds.community.repo

import kr.mashup.seehyangrds.community.entity.Story
import kr.mashup.seehyangrds.community.entity.StoryLike
import kr.mashup.seehyangrds.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface StoryLikeRepository : JpaRepository<StoryLike, Long> {

    fun existsByUserAndStory(user: User, story: Story):Boolean

    fun deleteByUserAndStory(user: User, story: Story)

    fun countByStory(story:Story): Long

}