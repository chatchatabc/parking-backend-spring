package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.user.UserNotificationResponse
import com.chatchatabc.parking.api.application.dto.user.UserProfileUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    /**
     * Get user profile
     */
    @Operation(
        summary = "Get the profile of the logged in user",
        description = "User to get the profile of the logged in user."
    )
    @GetMapping("/me")
    fun getProfile(): ResponseEntity<ApiResponse<User>> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val user = userRepository.findById(principal.id).get()
            ResponseEntity.ok().body(
                ApiResponse(
                    user, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }

    /**
     * Get user notification id
     */
    @Operation(
        summary = "Get the notification id of the logged in user",
        description = "Get notification id of the logged in user. This is used for push notifications and should not be available to other users."
    )
    @GetMapping("/get-notification-id")
    fun getNotificationId(): ResponseEntity<ApiResponse<UserNotificationResponse>> {
        return try {
            // Get user from security context holder
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val user = userRepository.findById(principal.id).get()
            ResponseEntity.ok().body(
                ApiResponse(
                    UserNotificationResponse(user.notificationId),
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }

    /**
     * Update user
     */
    @Operation(
        summary = "Update the details of a user's profile",
        description = "User to update the details of a user's profile."
    )
    @PutMapping("/update")
    fun updateUser(
        @RequestBody request: UserProfileUpdateRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            // Get principal from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val user = userService.updateUser(
                principal.userId,
                request.phone,
                request.username,
                request.email,
                request.firstName,
                request.lastName
            )
            ResponseEntity.ok().body(
                ApiResponse(user, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true)
                )
        }
    }
}