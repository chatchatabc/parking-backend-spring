package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.UserMapper
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
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
    ) = runCatching { principal.name.user.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * User notification response data class
     */
    data class UserNotificationResponse(
        val notificationId: String?
    )

    /**
     * Get user notification id
     */
    @Operation(
        summary = "Get the notification id of tFhe logged in user",
        description = "Get notification id of the logged in user. This is used for push notifications and should not be available to other users."
    )
    @GetMapping("/notification-id")
    fun getNotificationId(
        principal: Principal
    ) = runCatching {
        UserNotificationResponse(principal.name.user.notificationUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }

    // TODO: Create API for change username

    // TODO: Create API for change phone number

    /**
     * Update user
     */
    @Operation(
        summary = "Update the details of a user's profile",
        description = "User to update the details of a user's profile."
    )
    @PutMapping
    fun updateUser(
        @RequestBody request: UserMapper.UserMapDTO,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        userMapper.mapRequestToUser(request, user)
        userService.saveUser(user).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Upload user avatar
     */
    @PostMapping("/upload-avatar")
    fun uploadAvatar(
        @RequestParam("file", required = true) file: MultipartFile,
        principal: Principal
    ) = runCatching {
        userService.uploadImage(
            principal.name.user,
            "avatar",
            file.inputStream,
            file.originalFilename,
            file.size,
            file.contentType
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get user avatar by any identification (id, username, email, phone)
     */
    @GetMapping("/avatar/{id}")
    fun getUserAvatar(
        @PathVariable id: String,
        response: HttpServletResponse
    ) = runCatching {
        val headers = HttpHeaders()
        // Add 1 day cache
        response.setHeader("Cache-Control", "max-age=86400")
        val resource = InputStreamResource(fileStorageService.downloadFile(id.user.avatar.key))
        ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
    }.getOrElse {
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
        ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
    }
}