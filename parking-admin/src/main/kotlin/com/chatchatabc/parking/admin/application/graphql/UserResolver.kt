package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.log.UserLoginLog
import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class UserResolver(
    private val userRepository: UserRepository,
    private val userLoginLogRepository: UserLoginLogRepository,
    private val userLogoutLogRepository: UserLogoutLogRepository
) {

    /**
     * Get user by id
     */
    @QueryMapping
    fun getUserById(
        @Argument id: String
    ): Optional<User> {
        return userRepository.findByUserId(id)
    }

    /**
     * Get user by username
     */
    @QueryMapping
    fun getUserByUsername(
        @Argument username: String
    ): Optional<User> {
        return userRepository.findByUsername(username)
    }

    /**
     * Get user by phone
     */
    @QueryMapping
    fun getUserByPhone(
        @Argument phone: String
    ): Optional<User> {
        return userRepository.findByPhone(phone)
    }

    /**
     * Get all users
     */
    @QueryMapping
    fun getUsers(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<User> {
        val pr = PageRequest.of(page, size)
        val users = userRepository.findAll(pr)
        return PagedResponse(
            users.content,
            PageInfo(
                users.size,
                users.totalElements,
                users.isFirst,
                users.isLast,
                users.isEmpty
            )
        )
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