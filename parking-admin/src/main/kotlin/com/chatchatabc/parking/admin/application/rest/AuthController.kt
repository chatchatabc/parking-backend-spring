package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.UserLoginRequest
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val userLoginLogService: UserLoginLogService
) {
    /**
     * Login user and authenticate user
     */
    @PostMapping("/login")
    fun loginUser(
        @RequestBody req: UserLoginRequest,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) =
        runCatching {
            val user = req.username.user
            try {
                // Authenticate user
                authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                        req.username,
                        req.password
                    )
                )
                // Convert granted authority roles to list of string roles
                val roleStrings: List<String> = user.roles.stream()
                    .map { it.authority }
                    .toList()

                // Generate JWT Token
                val token: String = jwtService.generateToken(user.userUuid, user.username, roleStrings)
                response.setHeader("X-Access-Token", token)

                // Generate Successful Login Log
                userLoginLogService.createLog(user.id, request.remoteAddr, 1, true)
                user.toResponse()
            } catch (e: Exception) {
                // Generate Failed Login Log
                userLoginLogService.createLog(user.id, request.remoteAddr, 1, false)
                throw Exception("Invalid username/password supplied")
            }
        }.getOrElse { it.toErrorResponse() }
}