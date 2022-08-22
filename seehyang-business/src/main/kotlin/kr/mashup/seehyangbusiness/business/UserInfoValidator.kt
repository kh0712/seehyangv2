package kr.mashup.seehyangbusiness.business

import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class UserInfoValidator {

    fun isValidUserNickname(nickname:String):Boolean{
        if(nickname.length<3){
            return false
        }
        return true
    }

    fun isValidUserAge(age:Int):Boolean{
        if(age<0){
            return false
        }
        return true
    }

    fun isValidPassword(password:String):Boolean{
        if(password.length <8){
            return false
        }
        return true
    }

}