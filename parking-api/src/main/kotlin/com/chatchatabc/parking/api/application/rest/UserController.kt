package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.api.application.dto.UserNotificationResponse
import com.chatchatabc.parking.api.application.mapper.UserMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.infra.service.FileStorageService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val fileStorageService: FileStorageService,
) {
    private val userMapper = Mappers.getMapper(UserMapper::class.java)

    /**
     * Get user profile
     */
    @Operation(
        summary = "Get the profile of the logged in user",
        description = "User to get the profile of the logged in user."
    )
    @GetMapping("/me")
    fun getProfile(
        principal: Principal
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            ResponseEntity.ok().body(ApiResponse(user, listOf()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
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
    fun getNotificationId(
        principal: Principal
    ): ResponseEntity<ApiResponse<UserNotificationResponse>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            ResponseEntity.ok().body(ApiResponse(UserNotificationResponse(user.notificationUuid), listOf()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    // TODO: Create API for change username

    // TODO: Create API for change phone number

    data class UserProfileUpdateRequest(
        val email: String?,
        val firstName: String?,
        val lastName: String?,
    )

    /**
     * Update user
     */
    @Operation(
        summary = "Update the details of a user's profile",
        description = "User to update the details of a user's profile."
    )
    @PutMapping("/update")
    fun updateUser(
        @RequestBody request: UserProfileUpdateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            userMapper.updateUserFromUpdateProfileRequest(request, user)
            userService.saveUser(user)
            ResponseEntity.ok().body(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }

    // TODO: Implement update phone number api

    /**
     * Upload user avatar
     */
    @PostMapping("/upload-avatar")
    fun uploadAvatar(
        @RequestParam("file", required = true) file: MultipartFile,
        principal: Principal
    ): ApiResponse<User> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            userService.uploadImage(
                user,
                "avatar",
                file.inputStream,
                file.originalFilename,
                file.size,
                file.contentType
            )
            ApiResponse(user, listOf())
        } catch (e: Exception) {
            ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
        }
    }

    /**
     * Get user avatar by username
     */
    @GetMapping("/avatar/{username}")
    fun getUserAvatar(
        @PathVariable username: String,
        response: HttpServletResponse
    ): ResponseEntity<InputStreamResource> {
        return try {
            val user = userRepository.findByUsername(username).get()
            if (user.avatar == null) {
                throw Exception("Avatar not found")
            }
            val headers = HttpHeaders()
            // Add 1 day cache
            response.setHeader("Cache-Control", "max-age=86400")
            val resource = InputStreamResource(fileStorageService.downloadFile(user.avatar.key))
            ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get user avatar by userId
     */
    @GetMapping("/avatar/id/{userUuid}")
    fun getUserAvatarById(
        @PathVariable userUuid: String,
        response: HttpServletResponse
    ): ResponseEntity<InputStreamResource> {
        return try {
            val user = userRepository.findByUserUuid(userUuid).get()
            if (user.avatar == null) {
                throw Exception("Avatar not found")
            }
            val headers = HttpHeaders()
            // Add 1 day cache
            response.setHeader("Cache-Control", "max-age=86400")
            val resource = InputStreamResource(fileStorageService.downloadFile(user.avatar.key))
            ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
        }
    }
}