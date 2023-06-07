package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.UserLoginLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserLoginLogGQLController(
    private val userLoginLogRepository: UserLoginLogRepository,
    private val userRepository: UserRepository
) {
    /**
     * Get user login logs
     */
    @QueryMapping
    fun getUserLoginLogs(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<UserLoginLog> {
        val pr = PageRequest.of(page, size)
        val logs = userLoginLogRepository.findAll(pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }

    /**
     * Get user login logs per user
     */
    @QueryMapping
    fun getUserLoginLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): PagedResponse<UserLoginLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUserUuid(uuid).get()
        val logs = userLoginLogRepository.findByUser(user.id, pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }

    /**
     * Get user login logs per username
     */
    @QueryMapping
    fun getUserLoginLogsByUsername(
        @Argument page: Int,
        @Argument size: Int,
        @Argument username: String
    ): PagedResponse<UserLoginLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUsername(username).get()
        val logs = userLoginLogRepository.findByUser(user.id, pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }

    /**
     * Get user login logs per phone
     */
    @QueryMapping
    fun getUserLoginLogsByPhone(
        @Argument page: Int,
        @Argument size: Int,
        @Argument phone: String
    ): PagedResponse<UserLoginLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByPhone(phone).get()
        val logs = userLoginLogRepository.findByUser(user.id, pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }
}