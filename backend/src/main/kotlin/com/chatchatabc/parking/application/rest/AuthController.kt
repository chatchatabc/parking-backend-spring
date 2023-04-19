package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.*
import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val roleRepository: RoleRepository
) {
    private val mapper = ModelMapper()

    /**
     * Login with username and password
     */
    @PostMapping("/login")
    fun login(
        @RequestBody request: UserLoginRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val queriedUser = userRepository.findByUsername(request.username)
            if (queriedUser.isEmpty) {
                throw Exception("User not found")
            }

            // Authenticate user
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.username,
                    request.password
                )
            )

            val token: String = jwtService.generateToken(queriedUser.get())
            val headers = HttpHeaders()
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(UserResponse(queriedUser.get(), null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse(null, ErrorContent("Login Error", e.message ?: "Unknown Error")))
        }
    }

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
    @PostMapping("/register-parking-manager")
    fun registerParkingOwner(
        @RequestBody request: UserRegisterRequest
    ): ResponseEntity<UserResponse> {
        return try {
            val user = mapper.map(request, User::class.java)
            val registeredUser = userService.register(user, RoleNames.ROLE_PARKING_MANAGER)
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