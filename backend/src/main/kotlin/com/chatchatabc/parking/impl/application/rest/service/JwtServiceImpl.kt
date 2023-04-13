package com.chatchatabc.parking.impl.application.rest.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtServiceImpl (
    @Value("\${server.jwt.secret}")
    private val secret: String,

    @Value("\${server.jwt.expiration}")
    private val expiration: String,

    private val userRepository: UserRepository
) : JwtService {
    private val hmac512: Algorithm = Algorithm.HMAC512(secret)
    private val verifier: JWTVerifier = JWT.require(hmac512).build()

    /**
     * Generate Token
     */
    override fun generateToken(user: User): String {
        return JWT.create()
            .withSubject(user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + expiration.toLong()))
            .sign(hmac512)
    }

    /**
     * Validate Token and Get User
     */
    override fun validateTokenAndGetUser(token: String): User? {
        return try {
            val subject = verifier.verify(token).subject
            val user = userRepository.findById(subject)
            if (user.isEmpty) {
                throw Exception("Invalid Token")
            }
            user.get()
        } catch (e: Exception) {
            return null
        }
    }
}