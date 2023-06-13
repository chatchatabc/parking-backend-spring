package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.*
import com.chatchatabc.parking.admin.application.mapper.UserMapper
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserBanHistoryLog
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import com.chatchatabc.parking.domain.service.UserService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.mapstruct.factory.Mappers
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userBanHistoryLogRepository: UserBanHistoryLogRepository
) {
    private val userMapper = Mappers.getMapper(UserMapper::class.java)

    /**
     * Create User Request
     */
    data class UserCreateRequest(
        val email: String?,
        val phone: String,
        val username: String?,
        val roles: List<String>,
        val enabled: Boolean = true
    )

    /**
     * Create user
     */
    @PostMapping("/create")
    fun createUser(
        @RequestBody req: UserCreateRequest
    ) = runCatching {
        val user = User().apply {
            this.roles = roleRepository.findRolesIn(req.roles)
            if (req.enabled) {
                this.status = 0
            } else {
                this.status = -1
            }
        }
        userMapper.createUserFromCreateRequest(req, user)
        userService.saveUser(user).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update User Request
     */
    data class UserUpdateRequest(
        val email: String?,
        val username: String?,
        val firstName: String?,
        val lastName: String?,
        val phone: String?,
        val roles: List<String>?
    )

    /**
     * Update user
     */
    @PutMapping("/update/{userUuid}")
    fun updateUser(
        @RequestBody req: UserUpdateRequest,
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
     * Override user password
     */
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
     * Ban user
     */
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
     * Unban user
     */
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
}