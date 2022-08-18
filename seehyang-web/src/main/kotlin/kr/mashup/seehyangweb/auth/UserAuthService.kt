package kr.mashup.seehyangweb.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import kr.mashup.seehyangcore.exception.BadRequestException
import kr.mashup.seehyangcore.exception.ResultCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.annotation.PostConstruct


@Service
class UserAuthService(
    @Value("\${jwt.secretKey}")
    private val secretKey: String
) {
    lateinit var algorithm: Algorithm
    lateinit var jwtVerifier: JWTVerifier


    fun getId(token: String): Long {
        return try {
            jwtVerifier.verify(token).getClaim("id").asLong()
        } catch (e: JWTVerificationException) {
            throw BadRequestException(ResultCode.UNAUTHORIZED_USER)
        }
    }

    fun getToken(userAuth: UserAuth): String {

        val id = userAuth.id
        val exp = Date.from(Instant.now().plus(4L, ChronoUnit.DAYS))

        return JWT
            .create()
            .withClaim("id", id)
            .withExpiresAt(exp)
            .sign(algorithm)
    }

    @PostConstruct
    private fun init() {
        algorithm = Algorithm.HMAC256(secretKey)
        jwtVerifier = JWT.require(algorithm).build()
    }
}