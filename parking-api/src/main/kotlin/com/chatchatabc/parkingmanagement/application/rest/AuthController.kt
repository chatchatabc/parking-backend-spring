package com.chatchatabc.parkingmanagement.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.user.UserPhoneLoginRequest
import com.chatchatabc.parking.application.dto.user.UserPhoneLoginResponse
import com.chatchatabc.parking.application.dto.user.UserResponse
import com.chatchatabc.parking.application.dto.user.UserVerifyOTPRequest
import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.service.UserService
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
     * Login with phone number
     */
    @PostMapping("/login")
    fun loginWithPhone(
        @RequestBody req: UserPhoneLoginRequest
    ): ResponseEntity<UserPhoneLoginResponse> {
        return try {
            println(req)
            userService.softRegisterUser(req.phone, req.username)
            userService.createOTPAndSendSMS(req.phone)
            ResponseEntity.ok().body(UserPhoneLoginResponse(null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserPhoneLoginResponse(ErrorContent("User Login Error", e.message ?: "Unknown Error")))
        }
    }


    /**
     * Verify OTP dynamically
     */
    @PostMapping("/verify")
    fun verifyOTP(
        @RequestBody request: UserVerifyOTPRequest,
    ): ResponseEntity<UserResponse> {
        return try {
            val headers = HttpHeaders()
            val roleName: RoleNames = RoleNames.ROLE_PARKING_MANAGER
            val user = userService.verifyOTP(request.phone, request.otp, roleName)
            val token: String = jwtService.generateToken(user.id)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            // TODO: Improve error message
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    UserResponse(
                        null,
                        ErrorContent("OTP Verify Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}