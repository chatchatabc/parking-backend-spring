package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.user.UserCreateRequest
import com.chatchatabc.parking.admin.application.dto.user.UserUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    /**
     * Create user
     */
    @PostMapping("/create")
    fun createUser(
        @RequestBody req: UserCreateRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val roles = roleRepository.findRolesIn(req.roles)
            val user = User().apply {
                this.phone = req.phone
                this.username = req.username
                this.email = req.email
                this.roles = roles
            }
            val createdUser = userRepository.save(user)
            return ResponseEntity.ok(
                ApiResponse(
                    createdUser, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
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

    @PutMapping("/update/{id}")
    fun updateUser(
        @RequestBody req: UserUpdateRequest, @PathVariable id: String
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val updatedUser = userService.updateUser(
                id,
                req.phone,
                req.username,
                req.email,
                req.firstName,
                req.lastName
            )
            return ResponseEntity.ok(
                ApiResponse(
                    updatedUser, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
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
}