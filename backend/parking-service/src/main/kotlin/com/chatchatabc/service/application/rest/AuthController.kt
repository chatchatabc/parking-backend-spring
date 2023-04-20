package com.chatchatabc.service.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.application.rest.service.JwtService
import com.chatchatabc.api.domain.enums.RoleNames
import com.chatchatabc.api.domain.service.UserService
import com.chatchatabc.service.application.dto.*
import org.modelmapper.ModelMapper
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
    private val modelMapper = ModelMapper()

    /**
     * Dynamic Login with phone number
     */
    @PostMapping("/{type}/login")
    fun loginWithPhone(
        @RequestBody request: com.chatchatabc.service.application.dto.UserPhoneLoginRequest,
        @PathVariable type: String
    ): ResponseEntity<com.chatchatabc.service.application.dto.UserPhoneLoginResponse> {
        return try {
            if (type != "user" && type != "parking") {
                throw Exception("Invalid type")
            }
            // Check if the user is fully registered
            userService.softRegisterUser(request.phone, request.username)
            userService.createOTPAndSendSMS(request.phone)
            ResponseEntity.ok().body(com.chatchatabc.service.application.dto.UserPhoneLoginResponse(null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    com.chatchatabc.service.application.dto.UserPhoneLoginResponse(
                        ErrorContent(
                            "Login Error",
                            e.message ?: "Unknown Error"
                        )
                    )
                )
        }
    }

    /**
     * Verify OTP dynamically
     */
    @PostMapping("/{type}/verify")
    fun verifyOTP(
        @RequestBody request: com.chatchatabc.service.application.dto.UserVerifyOTPRequest,
        @PathVariable type: String
    ): ResponseEntity<com.chatchatabc.service.application.dto.UserResponse> {
        return try {
            val headers = HttpHeaders()
            val roleName: RoleNames = when (type) {
                "parking" -> {
                    RoleNames.ROLE_PARKING_MANAGER
                }

                "user" -> {
                    RoleNames.ROLE_USER
                }

                else -> {
                    throw Exception("Invalid type")
                }
            }
            val user = userService.verifyOTP(request.phone, request.otp, roleName)
            val userDTO = modelMapper.map(user, UserDTO::class.java)
            val token: String = jwtService.generateToken(userDTO)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(com.chatchatabc.service.application.dto.UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    com.chatchatabc.service.application.dto.UserResponse(
                        null,
                        ErrorContent("OTP Verify Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}