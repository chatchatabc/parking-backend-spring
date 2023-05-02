package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val userRepository: UserRepository,
    private val userLogoutLogRepository: UserLogoutLogRepository
) {
    /**
     * Get User Profile
     */
    @Operation(
        summary = "Get the profile of the logged in user",
        description = "User to get the profile of the logged in user."
    )
    @GetMapping("/me")
    fun getProfile(
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val user = userRepository.findByUserId(principal.userId).get()
            userLogoutLogRepository.save(
                UserLogoutLog().apply {
                    this.user = user
                    this.email = user.email
                    this.phone = user.phone
                    this.type = 1
                    this.ipAddress = request.remoteAddr
                }
            )
            ResponseEntity.ok().body(
                ApiResponse(user, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR.name,
                        true
                    )
                )
        }
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    fun logoutUser(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            // TODO: Implement logout
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true))
        }
    }
}