package kr.mashup.seehyangrds.user.entity

import kr.mashup.seehyangrds.common.BaseEntity
import kr.mashup.seehyangcore.vo.Gender
import kr.mashup.seehyangrds.image.entity.Image
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.*


@Entity
@Table(name = "users")
class User(
    var nickname: String,

    var email: String,

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    password: String,

    var age: Int? = null,

    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,

    @OneToOne(fetch = FetchType.LAZY)
    var profile: Image? = null
) : BaseEntity() {


    lateinit var password: String

    fun isMatchedPassword(rawPassword: String): Boolean
    = passwordEncoder.matches(rawPassword, this.password)

    init {
        this.password = passwordEncoder.encode(password)
    }

    companion object{
        val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
    }

}