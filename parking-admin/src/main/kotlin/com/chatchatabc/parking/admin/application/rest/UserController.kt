package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.UserMapper
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserBanHistoryLog
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.specification.UserSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userBanHistoryLogRepository: UserBanHistoryLogRepository,
    private val fileStorageService: FileStorageService,
) {
    private val userMapper = Mappers.getMapper(UserMapper::class.java)

    /**
     * Get User
     */
    @Operation(
        summary = "Get User",
        description = "Get User"
    )
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: String) = id.user.toResponse()

    /**
     * Get list of users
     */
    @Operation(
        summary = "Get Users",
        description = "Get Users"
    )
    @GetMapping
    fun getUsers(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = runCatching {
        val spec = UserSpecification()
            .withParams(params)

        // Filter by verified using params
        // 0: not verified
        if (params["verified"] == "0") {
            spec.and(UserSpecification.notVerified())
        }
        // 1: verified
        else if (params["verified"] == "1") {
            spec.and(UserSpecification.verified())
        }
        userRepository.findAll(spec, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Create user
     */
    @Operation(
        summary = "Create User",
        description = "Create User"
    )
    @PostMapping
    fun createUser(
        @RequestBody req: UserMapper.UserCreateDTO
    ) = runCatching {
        val user = User().apply {
            this.roles = roleRepository.findRolesIn(req.roles)
            if (req.enabled) {
                this.status = 0
            } else {
                this.status = -1
            }
        }
        userMapper.mapRequestToUser(req, user)
        userService.saveUser(user).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update user
     */
    @Operation(
        summary = "Update User",
        description = "Update User"
    )
    @PutMapping("/{userUuid}")
    fun updateUser(
        @RequestBody req: UserMapper.UserUpdateDTO,
        @PathVariable userUuid: String,
        principal: Principal
    ) = runCatching {
        val user = userUuid.user
        userMapper.updateUserFromUpdateRequest(req, user)
        // If user is not self, update role as well
        if (principal.name != userUuid && req.roles.isNullOrEmpty().not()) {
            val roleRecords = roleRepository.findRolesIn(req.roles)
            user.roles = roleRecords
        }
        userService.saveUser(user).toResponse()
    }.getOrElse { it.toErrorResponse() }

    // TODO: Implement update phone number api

    /**
     * User override password request
     */
    data class UserOverridePasswordRequest(
        val newPassword: String,
    )

    /**
     * Override user password
     */
    @Operation(
        summary = "Override User Password",
        description = "Override User Password"
    )
    @PutMapping("/override-password/{userUuid}")
    fun overrideUserPassword(
        @PathVariable userUuid: String,
        @RequestBody req: UserOverridePasswordRequest
    ) = runCatching {
        val user = userUuid.user.apply {
            this.password = passwordEncoder.encode(req.newPassword)
        }
        userService.saveUser(user).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * User unban request
     */
    data class UserBanRequest(
        val until: LocalDateTime,
        val reason: String
    )

    /**
     * Ban user
     */
    @Operation(
        summary = "Ban User",
        description = "Ban User"
    )
    @PostMapping("/ban/{userUuid}")
    fun banUser(
        @PathVariable userUuid: String,
        @RequestBody req: UserBanRequest,
        principal: Principal
    ) = runCatching {
        val banLog = UserBanHistoryLog().apply {
            this.user = userUuid.user.id
            this.bannedBy = principal.name.user.id
            this.reason = req.reason
            this.until = req.until
            this.status = UserBanHistoryLog.BANNED
        }
        // TODO: Convert to service
        userBanHistoryLogRepository.save(banLog).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * User unban request
     */
    data class UserUnbanRequest(
        val unbanReason: String
    )

    /**
     * Unban user
     */
    @Operation(
        summary = "Unban User",
        description = "Unban User"
    )
    @PostMapping("/unban/{userUuid}")
    fun unbanUser(
        @PathVariable userUuid: String,
        @RequestBody req: UserUnbanRequest,
        principal: Principal
    ) = runCatching {
        // Get latest ban log
        val banLog = userBanHistoryLogRepository.findLatestBanLog(userUuid.user.id).orElseThrow().apply {
            this.unbannedBy = principal.name.user.id
            this.unbanReason = req.unbanReason
            this.unbannedAt = LocalDateTime.now()
            this.status = UserBanHistoryLog.UNBANNED
        }
        // TODO: Convert to service
        userBanHistoryLogRepository.save(banLog).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Upload user avatar
     */
    @PostMapping("/upload-avatar/{id}")
    fun uploadAvatar(
        @RequestParam("file", required = true) file: MultipartFile,
        @PathVariable id: String,
        principal: Principal,
    ) = runCatching {
        val uploadedBy = principal.name.user
        val targetUser = id.user
        userService.uploadImage(
            uploadedBy,
            targetUser,
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
        // response.setHeader("Cache-Control", "max-age=86400")
        val resource = InputStreamResource(fileStorageService.downloadFile(id.user.avatar.key))
        ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
    }.getOrElse {
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
        ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
    }

    /**
     * Get User by Parking Lot Identification
     */
    @Operation(
        summary = "Get User by Parking Lot Identifier",
        description = "Get User by Parking Lot Identifier"
    )
    @GetMapping("/parking-lot/{id}")
    fun getUserByParkingLot(
        @PathVariable id: String
    ) = runCatching {
        id.parkingLot.owner.user.toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get User by Vehicle Identification
     */
    @Operation(
        summary = "Get User by Vehicle Identifier",
        description = "Get User by Vehicle Identifier"
    )
    @GetMapping("/vehicle/{id}")
    fun getUserByVehicle(
        @PathVariable id: String
    ) = runCatching {
        id.vehicle.owner.user.toResponse()
    }.getOrElse { it.toErrorResponse() }

}