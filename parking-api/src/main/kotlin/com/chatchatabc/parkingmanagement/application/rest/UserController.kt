package com.chatchatabc.parkingmanagement.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.application.dto.user.UserDTO
import com.chatchatabc.api.application.dto.user.UserProfileUpdateRequest
import com.chatchatabc.api.application.dto.user.UserResponse
import com.chatchatabc.api.domain.service.UserService
import org.apache.dubbo.config.annotation.DubboReference
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    @DubboReference
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getProfile(): ResponseEntity<UserResponse> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as UserDTO
            val user = userService.getUser(principal.id)
            ResponseEntity.ok().body(UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse(null, ErrorContent("User Profile Get Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * Update user
     */
    @PutMapping("/update")
    fun updateUser(
        @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<UserResponse> {
        return try {
            // Get principal from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as UserDTO
            val user = userService.updateUser(
                principal.id,
                request.username,
                request.email,
                request.firstName,
                request.lastName
            )
            ResponseEntity.ok().body(UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(UserResponse(null, ErrorContent("User Profile Update Error", e.message ?: "Unknown Error")))
        }
    }
}