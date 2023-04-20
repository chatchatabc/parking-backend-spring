package com.chatchatabc.parkingmanagement.application.rest

import com.chatchatabc.api.application.dto.user.UserPhoneLoginRequest
import com.chatchatabc.api.application.dto.user.UserPhoneLoginResponse
import com.chatchatabc.api.application.rest.service.JwtService
import com.chatchatabc.api.domain.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val jwtService: JwtService
) {
    private val modelMapper =  ModelMapper()

    @PostMapping("/login")
    fun loginWithPhone(
        @RequestBody request: UserPhoneLoginRequest
    ): ResponseEntity<UserPhoneLoginResponse> {
        // Return ok for now
        return ResponseEntity.ok().body(UserPhoneLoginResponse(null))
    }
}