package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserLoginLog
import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val userLoginLogRepository: UserLoginLogRepository,
    private val userLogoutLogRepository: UserLogoutLogRepository
) {

    /**
     * Get all users by pageable
     */
    @GetMapping("/get")
    fun getUsers(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<User>>> {
        return try {
            val users = userRepository.findAll(pageable)
            ResponseEntity.ok(
                ApiResponse(
                    users,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Get User Login Logs w/ pageable
     */
    // TODO: Add date range?
    @GetMapping("/get-login-logs")
    fun getUserLoginLogs(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<UserLoginLog>>> {
        return try {
            val logs = userLoginLogRepository.findAll(pageable)
            ResponseEntity.ok(
                ApiResponse(
                    logs,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Get User Logout Logs w/ pageable
     */
    // TODO: Add date range?
    @GetMapping("/get-logout-logs")
    fun getUserLogoutLogs(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<UserLogoutLog>>> {
        return try {
            val logs = userLogoutLogRepository.findAll(pageable)
            ResponseEntity.ok(
                ApiResponse(
                    logs,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }
}