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
import org.springframework.http.HttpStatus
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
            return ResponseEntity.ok(
                ApiResponse(
                    null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                    )
                )
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
            val user = userRepository.findByUserUuid(userUuid).get()
            userMapper.updateUserFromUpdateRequest(req, user)
            userService.saveUser(user)
            return ResponseEntity.ok(
                ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                    )
                )
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
            return ResponseEntity.ok(
                ApiResponse(
                    userRepository.save(user), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                )
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
                this.status = 0
            }
            ResponseEntity.ok().body(
                ApiResponse(
                    userBanHistoryLogRepository.save(banLog), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                )
            )
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
                this.status = -1
            }
            ResponseEntity.ok().body(
                ApiResponse(
                    userBanHistoryLogRepository.save(banLog), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                )
            )
        }
    }
}