package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.user.UserLoginRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserLoginLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
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
    private val jwtService: JwtService,
    private val userLoginLogRepository: UserLoginLogRepository
) {
    /**
     * Login user and authenticate user
     */
    @PostMapping("/login")
    fun loginUser(
        @RequestBody req: UserLoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        val user = userRepository.findByUsername(req.username)
        return try {
            // Authenticate user
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    req.username,
                    req.password
                )
            )
            // Generate JWT Token
            val headers = HttpHeaders()
            val token: String = jwtService.generateToken(user.get().id)
            headers.set("X-Access-Token", token)
            // Generate Successful Login Log
            // TODO: Move as an event
            userLoginLogRepository.save(
                UserLoginLog().apply {
                    this.user = user.get()
                    this.ipAddress = request.remoteAddr
                    this.email = user.get().email
                    this.phone = user.get().phone
                    this.type = 1
                    this.success = true
                }
            )
            ResponseEntity.ok().headers(headers)
                .body(ApiResponse(user.get(), HttpStatus.OK.value(), ResponseNames.USER_LOGIN_SUCCESS.name, false))
        } catch (e: Exception) {
            // Generate Failed Login Log
            // TODO: Move as an event
            userLoginLogRepository.save(
                UserLoginLog().apply {
                    this.user = user.get()
                    this.ipAddress = request.remoteAddr
                    this.email = user.get().email
                    this.phone = user.get().phone
                    this.type = 1
                    this.success = false
                }
            )
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.USER_BAD_CREDENTIALS.name,
                        true
                    )
                )
        }
    }
}