package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.user.UserCreateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
) {
    /**
     * Create user
     */
    @PostMapping("/create")
    fun createUser(
        @RequestBody req: UserCreateRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = User().apply {
                this.phone = req.phone
                this.username = req.username
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
}