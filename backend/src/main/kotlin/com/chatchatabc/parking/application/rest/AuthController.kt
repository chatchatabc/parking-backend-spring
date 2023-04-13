package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.UserRegisterRequest
import com.chatchatabc.parking.application.dto.UserResponse
import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    private val mapper = ModelMapper()

    /**
     * Register a new user
     */
    @PostMapping("/register-user")
    fun registerUser(
        @RequestBody request: UserRegisterRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val user = mapper.map(request, User::class.java)
            val registeredUser = userService.register(user, RoleNames.ROLE_USER)
            ResponseEntity.ok(UserResponse(registeredUser, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.ok(UserResponse(null, ErrorContent("Register User Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * Register a new parking owner
     */
    @PostMapping("/register-parking-owner")
    fun registerParkingOwner(
        @RequestBody request: UserRegisterRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val user = mapper.map(request, User::class.java)
            val registeredUser = userService.register(user, RoleNames.ROLE_PARKING_OWNER)
            ResponseEntity.ok(UserResponse(registeredUser, null))
        } catch (e: Exception) {
            ResponseEntity.ok(
                UserResponse(
                    null,
                    ErrorContent("Register Parking Owner Error", e.message ?: "Unknown Error")
                )
            )
        }
    }
}