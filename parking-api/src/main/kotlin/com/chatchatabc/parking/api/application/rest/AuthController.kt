package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val userLoginLogService: UserLoginLogService,
    private val authenticationManager: AuthenticationManager,
) {
    /**
     * Login User Request
     */
    data class UserLoginRequest(
        val username: String,
        val password: String
    )
    @PostMapping("/login-password")
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
                userLoginLogService.createLog(user.id, request.remoteAddr, 0, true)
                user.toResponse()
            } catch (e: Exception) {
                // Generate Failed Login Log
                userLoginLogService.createLog(user.id, request.remoteAddr, 0, false)
                throw Exception("Invalid username/password supplied")
            }
        }.getOrElse { it.toErrorResponse() }

    /**
     * Login User Request Data Class
     */
    data class UserPhoneLoginRequest(
        val phone: String,
        val username: String?
    )

    /**
     * Login with phone number or username
     */
    @Operation(
        summary = "Login with phone number or username",
        description = "This API is used for both parking managers and users."
    )
    @PostMapping("/login")
    fun loginWithPhone(
        @RequestBody req: UserPhoneLoginRequest
    ) = runCatching {
        userService.softRegisterUser(req.phone, req.username)
        // Generate OTP and set to KV store
        val otp = userService.generateOTPAndSaveToKV(req.phone, 900L)
        // Send OTP to SMS using events
        applicationEventPublisher.publishEvent(UserLoginEvent(this, req.phone, otp)).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Verify OTP Request Data Class
     */
    data class UserVerifyOTPRequest(
        val phone: String,
        val otp: String
    )

    /**
     * Verify OTP dynamically users
     */
    @Operation(
        summary = "Verify the OTP of a user logging in.",
        description = "This API is used for both parking owners and users. Type = owner if verifying a parking owner. Type = user if verifying a user."
    )
    @PostMapping("/verify/{type}")
    fun verifyOTP(
        @RequestBody req: UserVerifyOTPRequest,
        @PathVariable type: String,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) = runCatching {
        var user: User? = null
        try {
            var roleName: Role.RoleNames = Role.RoleNames.ROLE_USER
            if (type == "owner") {
                roleName = Role.RoleNames.ROLE_PARKING_OWNER
            }
            user = userService.verifyOTPAndAddRole(req.phone, req.otp, roleName)
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = user?.roles?.stream()
                ?.map { it.authority }
                ?.toList() ?: emptyList()

            val token: String = jwtService.generateToken(user!!.userUuid, user.username, roleStrings)
            response.setHeader("X-Access-Token", token)
            // Generate Successful Login Log
            userLoginLogService.createLog(user.id, request.remoteAddr, 0, true)
            user.toResponse()
        } catch (e: Exception) {
            // Generate Failed Login Log
            userLoginLogService.createLog(user!!.id, request.remoteAddr, 0, false)
            throw Exception("Invalid or expired OTP")
        }
    }.getOrElse { it.toErrorResponse() }
}