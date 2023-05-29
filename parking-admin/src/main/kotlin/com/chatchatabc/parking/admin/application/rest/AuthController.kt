package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.admin.application.dto.UserLoginRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.log.UserLoginLogService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
    private val userLoginLogService: UserLoginLogService
) {
    /**
     * Login user and authenticate user
     */
    @PostMapping("/login")
    fun loginUser(
        @RequestBody req: UserLoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        val user = userRepository.findByUsername(req.username)
        return try {
            // Authenticate user
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    req.username,
                    req.password
                )
            )
            // Generate JWT Token
            val headers = HttpHeaders()
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = user.get().roles.stream()
                .map { it.authority }
                .toList()

            val token: String = jwtService.generateToken(user.get().userUuid, user.get().username, roleStrings)
            headers.set("X-Access-Token", token)
            // Generate Successful Login Log
            userLoginLogService.createLog(user.get().id, request.remoteAddr, 1, true)
            ResponseEntity.ok().headers(headers)
                .body(ApiResponse(user.get(), null))
        } catch (e: Exception) {
            // Generate Failed Login Log
            if (user.isPresent) {
                userLoginLogService.createLog(user.get().id, request.remoteAddr, 1, false)
            }
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, listOf(ErrorElement(ResponseNames.USER_BAD_CREDENTIALS.name, null)))
                )
        }
    }
}