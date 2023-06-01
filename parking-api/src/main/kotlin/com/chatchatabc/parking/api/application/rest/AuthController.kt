package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.api.application.dto.UserPhoneLoginRequest
import com.chatchatabc.parking.api.application.dto.UserVerifyOTPRequest
import com.chatchatabc.parking.api.application.event.user.UserLoginEvent
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            userService.softRegisterUser(req.phone, req.username)
            // Generate OTP and set to KV store
            val otp = userService.generateOTPAndSaveToKV(req.phone, 900L)
            // Send OTP to SMS using events
            applicationEventPublisher.publishEvent(UserLoginEvent(this, req.phone, otp))
            ResponseEntity.ok().body(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
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
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        var user: User? = null
        return try {
            val headers = HttpHeaders()
            var roleName: RoleNames = RoleNames.ROLE_USER
            if (type == "owner") {
                roleName = RoleNames.ROLE_PARKING_OWNER
            }
            user = userService.verifyOTPAndAddRole(req.phone, req.otp, roleName)
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = user.roles.stream()
                .map { it.authority }
                .toList()

            val token: String = jwtService.generateToken(user.userUuid, user.username, roleStrings)
            headers.set("X-Access-Token", token)
            // Generate Successful Login Log
            userLoginLogService.createLog(user.id, request.remoteAddr, 0, true)
            ResponseEntity.ok().headers(headers).body(ApiResponse(user, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            // Generate Failed Login Log
            if (user != null) {
                userLoginLogService.createLog(user.id, request.remoteAddr, 0, false)
            }
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }
}