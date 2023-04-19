package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.*
import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth/parking")
class AuthParkingController(
    private val userService: UserService,
    private val jwtService: JwtService,
) {
    /**
     * Login with phone number
     */
    @PostMapping("/login")
    fun loginWithPhone(
        @RequestBody request: UserParkingPhoneLoginRequest
    ): ResponseEntity<UserParkingPhoneLoginResponse> {
        return try {
            // Check if user is fully registered
            userService.checkIfUserIsFullyRegistered(request.phone)
            userService.createOTPAndSendSMS(request.phone)
            ResponseEntity.ok().body(UserParkingPhoneLoginResponse(null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserParkingPhoneLoginResponse(ErrorContent("Login Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * Verify OTP
     */
    @PostMapping("/verify")
    fun verifyOTP(
        @RequestBody request: UserParkingVerifyRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val headers = HttpHeaders()
            val user = userService.parkingVerifyOTP(request.phone, request.otp)
            val token: String = jwtService.generateToken(user)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse(null, ErrorContent("OTP Verify Error", e.message ?: "Unknown Error")))
        }
    }

    @PutMapping("/update")
    fun updateProfile() {

    }
}