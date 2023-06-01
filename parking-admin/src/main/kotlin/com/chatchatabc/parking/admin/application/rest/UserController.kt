package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.*
import com.chatchatabc.parking.admin.application.mapper.UserMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserBanHistoryLog
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import com.chatchatabc.parking.domain.service.UserService
import org.mapstruct.factory.Mappers
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
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
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val user = User().apply {
                this.roles = roleRepository.findRolesIn(req.roles)
                if (req.enabled) {
                    this.status = 0
                } else {
                    this.status = -1
                }
            }
            userMapper.createUserFromCreateRequest(req, user)
            userService.saveUser(user)
            return ResponseEntity.ok(ApiResponse(null, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Update User Request
     */
    data class UserUpdateRequest(
        val email: String?,
        val username: String?,
        val firstName: String?,
        val lastName: String?,
        val phone: String?,
    )

    /**
     * Update user
     */
    @PutMapping("/update/{userUuid}")
    fun updateUser(
        @RequestBody req: UserUpdateRequest,
        @PathVariable userUuid: String
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            // TODO: Add logic for update roles
            val user = userRepository.findByUserUuid(userUuid).get()
            userMapper.updateUserFromUpdateRequest(req, user)
            userService.saveUser(user)
            return ResponseEntity.ok(ApiResponse(null, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    // TODO: Implement update phone number api

    /**
     * Override user password
     */
    @PutMapping("/override-password/{userUuid}")
    fun overrideUserPassword(
        @PathVariable userUuid: String,
        @RequestBody req: UserOverridePasswordRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUserUuid(userUuid).get().apply {
                this.password = passwordEncoder.encode(req.newPassword)
            }
            return ResponseEntity.ok(ApiResponse(userRepository.save(user), null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null)))
            )
        }
    }

    /**
     * Ban user
     */
    @PostMapping("/ban/{userUuid}")
    fun banUser(
        @PathVariable userUuid: String,
        @RequestBody req: UserBanRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<UserBanHistoryLog>> {
        return try {
            val bannedBy = userRepository.findByUserUuid(principal.name).get()
            val user = userRepository.findByUserUuid(userUuid).get()
            val banLog = UserBanHistoryLog().apply {
                this.user = user.id
                this.bannedBy = bannedBy.id
                this.reason = req.reason
                this.until = req.until
                this.status = UserBanHistoryLog.BANNED
            }
            ResponseEntity.ok().body(ApiResponse(userBanHistoryLogRepository.save(banLog), null))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Unban user
     */
    @PostMapping("/unban/{userUuid}")
    fun unbanUser(
        @PathVariable userUuid: String,
        @RequestBody req: UserUnbanRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<UserBanHistoryLog>> {
        return try {
            val unbannedBy = userRepository.findByUserUuid(principal.name).get()
            val user = userRepository.findByUserUuid(userUuid).get()
            // Get latest ban log
            val banLog = userBanHistoryLogRepository.findLatestBanLog(user.id).get().apply {
                this.unbannedBy = unbannedBy.id
                this.unbanReason = req.unbanReason
                this.status = UserBanHistoryLog.UNBANNED
            }
            ResponseEntity.ok().body(ApiResponse(userBanHistoryLogRepository.save(banLog), null))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }
}