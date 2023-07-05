package ru.yarsu.handlers

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTCreator
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import java.util.*

class JwtTools(private val secret: String, private val nameOrganization: String, val date: Date) {
    private val algorithm = Algorithm.HMAC512(secret)
    fun createToken(name: String): String? {
        try {
            val jwtBuilder: JWTCreator.Builder = JWT.create()
            jwtBuilder.withSubject(name)
            jwtBuilder.withIssuer(nameOrganization)
            jwtBuilder.withExpiresAt(date)
            return jwtBuilder.sign(algorithm)
        } catch (e: RuntimeException) {
            Response(OK).body("Вас нет в системе")
        }
        return null
    }
    fun checkToken(token: String): String? {
        val verifier: JWTVerifier = JWT.require(algorithm)
            .withIssuer(nameOrganization)
            .build()
        try {
            val decodedJWT: DecodedJWT = verifier.verify(token)
            return decodedJWT.subject
        } catch (e: JWTVerificationException) {
            Response(OK).body("Токен не прошел проверку")
        }
        return null
    }
}
