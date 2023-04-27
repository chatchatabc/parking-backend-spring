package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.user.UserLoginRequest
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {
    /**
     * Login user and authenticate user
     */
    @PostMapping("/login")
    fun loginUser(
        @RequestBody req: UserLoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUsername(req.username).get()

            // Authenticate user
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    req.username,
                    req.password
                )
            )
            // Generate JWT Token
            val headers = HttpHeaders()
            val token: String = jwtService.generateToken(user.id)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers)
                .body(ApiResponse(user, HttpStatus.OK.value(), "Login successful", false))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), e.message, true))
        }
    }
}