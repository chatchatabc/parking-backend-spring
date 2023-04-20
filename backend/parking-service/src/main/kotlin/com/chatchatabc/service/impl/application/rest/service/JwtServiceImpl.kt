package com.chatchatabc.service.impl.application.rest.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.application.rest.service.JwtService
import com.chatchatabc.service.domain.repository.UserRepository
import org.apache.dubbo.config.annotation.DubboService
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Value
import java.util.*

@DubboService
class JwtServiceImpl(
    @Value("\${server.jwt.secret}")
    private val secret: String,

    @Value("\${server.jwt.expiration}")
    private val expiration: String,

    private val userRepository: UserRepository
) : JwtService {
    private val hmac512: Algorithm = Algorithm.HMAC512(secret)
    private val verifier: JWTVerifier = JWT.require(hmac512).build()
    private val modelMapper = ModelMapper()

    /**
     * Generate Token
     */
    override fun generateToken(user: UserDTO): String? {
        return JWT.create()
            .withSubject(user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + expiration.toLong()))
            .sign(hmac512)
    }

    /**
     * Validate Token and Get User
     */
    override fun validateTokenAndGetUser(token: String): UserDTO? {
        return try {
            val subject = verifier.verify(token).subject
            val user = userRepository.findById(subject)
            if (user.isEmpty) {
                throw Exception("Invalid Token")
            }
            modelMapper.map(user.get(), UserDTO::class.java)
        } catch (e: Exception) {
            return null
        }
    }
}