package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.user.UserPhoneLoginRequest
import com.chatchatabc.parking.api.application.dto.user.UserVerifyOTPRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService
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
            userService.createOTPAndSendSMS(req.phone)
            ResponseEntity.ok().body(
                ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }


    /**
     * Verify OTP dynamically users
     */
    @Operation(
        summary = "Verify the OTP of a user logging in.",
        description = "This API is used for both parking managers and users. Type = manager if verifying a parking manager. Type = user if verifying a user."
    )
    @PostMapping("/verify/{type}")
    fun verifyOTP(
        @RequestBody request: UserVerifyOTPRequest,
        @PathVariable type: String,
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val headers = HttpHeaders()
            var roleName: RoleNames = RoleNames.ROLE_USER
            if (type == "manager") {
                roleName = RoleNames.ROLE_PARKING_MANAGER
            }
            val user = userService.verifyOTP(request.phone, request.otp, roleName)
            val token: String = jwtService.generateToken(user.userId)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(
                ApiResponse(user, HttpStatus.OK.value(), ResponseNames.USER_VERIFY_OTP_SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }
}