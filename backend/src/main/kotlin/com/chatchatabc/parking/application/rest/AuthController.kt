package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.*
import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController (
    private val userService: UserService,
    private val jwtService: JwtService
) {
    /**
     * Parking Management Login with phone number
     */
    @PostMapping("/parking/login")
    fun loginParkingManagementWithPhone(
        @RequestBody request: UserParkingManagementLoginRequest
    ): ResponseEntity<UserPhoneLoginResponse> {
        return try {
            // Check if user is fully registered
            userService.checkIfUserIsFullyRegistered(request.phone)
            userService.createOTPAndSendSMS(request.phone)
            ResponseEntity.ok().body(UserPhoneLoginResponse(null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserPhoneLoginResponse(ErrorContent("Login Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * User/Commuter Login with phone number
     */
    @PostMapping("/user/login")
    fun loginUserWithPhone(
        @RequestBody request: UserPhoneLoginRequest
    ): ResponseEntity<UserPhoneLoginResponse> {
        return try {
            // Check if the user is fully registered
            userService.checkIfUserIsFullyRegistered(request.phone, request.username)
            userService.createOTPAndSendSMS(request.phone)
            ResponseEntity.ok().body(UserPhoneLoginResponse(null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserPhoneLoginResponse(ErrorContent("Login Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * Verify OTP dynamically
     */
    @PostMapping("/{type}/verify")
    fun verifyOTP(
        @RequestBody request: UserVerifyOTPRequest,
        @PathVariable type: String
    ): ResponseEntity<UserResponse> {
        return try {
            val headers = HttpHeaders()
            var roleName = RoleNames.ROLE_USER
            if (type == "parking") {
                roleName = RoleNames.ROLE_PARKING_MANAGER
            }
            val user = userService.verifyOTP(request.phone, request.otp, roleName)
            val token: String = jwtService.generateToken(user)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse(null, ErrorContent("OTP Verify Error", e.message ?: "Unknown Error")))
        }
    }
}