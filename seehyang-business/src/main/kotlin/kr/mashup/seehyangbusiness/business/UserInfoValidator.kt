package kr.mashup.seehyangbusiness.business

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
@Deprecated("use javax validator")
class UserInfoValidator {

    private val MIN_NICKNAME_LENGTH : Int = 3
    private val MIN_PASSWORD_LENGTH : Int = 8
    private val MIN_AGE : Int = 0

    fun isValidUserNickname(nickname:String):Boolean{
        if(nickname.length<MIN_NICKNAME_LENGTH){
            return false
        }
        return true
    }

    fun isValidUserAge(age:Int):Boolean{
        if(age<MIN_AGE){
            return false
        }
        return true
    }

    fun isValidPassword(password:String):Boolean{
        if(password.length <MIN_PASSWORD_LENGTH){
            return false
        }
        return true
    }

}