package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.UserProfileUpdateRequest
import com.chatchatabc.parking.application.dto.UserResponse
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService
) {
    /**
     * Update user
     */
    @PutMapping("/update")
    fun updateUser(
        @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<UserResponse> {
        return try {
            // Get principal from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
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