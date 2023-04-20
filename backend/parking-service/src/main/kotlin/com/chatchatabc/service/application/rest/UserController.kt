package com.chatchatabc.service.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.api.domain.service.UserService
import com.chatchatabc.service.domain.model.User
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
        @RequestBody request: com.chatchatabc.service.application.dto.UserProfileUpdateRequest
    ): ResponseEntity<com.chatchatabc.service.application.dto.UserResponse> {
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
            ResponseEntity.ok().body(com.chatchatabc.service.application.dto.UserResponse(user, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    com.chatchatabc.service.application.dto.UserResponse(
                        null,
                        ErrorContent("User Profile Update Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}