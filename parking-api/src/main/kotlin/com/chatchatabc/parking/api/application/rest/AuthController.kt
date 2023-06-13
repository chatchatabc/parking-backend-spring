package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.UserPhoneLoginRequest
import com.chatchatabc.parking.api.application.dto.UserVerifyOTPRequest
import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val userLoginLogService: UserLoginLogService
) {

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
    }.getOrElse {
        it.toErrorResponse()
    }

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
    ) {
        var user: User? = null

        runCatching {
            var roleName: Role.RoleNames = Role.RoleNames.ROLE_USER
            if (type == "owner") {
                roleName = Role.RoleNames.ROLE_PARKING_OWNER
            }
            user = userService.verifyOTPAndAddRole(req.phone, req.otp, roleName)
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = user?.roles?.stream()
                ?.map { it.authority }
                ?.toList() ?: emptyList()

            val token: String = jwtService.generateToken(user!!.userUuid, user!!.username, roleStrings)
            response.setHeader("X-Access-Token", token)
            // Generate Successful Login Log
            userLoginLogService.createLog(user!!.id, request.remoteAddr, 0, true)
            user.toResponse()
        }.getOrElse {
            // Generate Failed Login Log
            if (user != null) {
                userLoginLogService.createLog(user!!.id, request.remoteAddr, 0, false)
            }
            it.toErrorResponse()
        }
    }
}