package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserLogoutLogGQLController(
    private val userLogoutLogRepository: UserLogoutLogRepository,
    private val userRepository: UserRepository
) {
    /**
     * Get user logout logs
     */
    @QueryMapping
    fun getUserLogoutLogs(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<UserLogoutLog> {
        val pr = PageRequest.of(page, size)
        val logs = userLogoutLogRepository.findAll(pr)
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
     * Get user logout logs per user
     */
    @QueryMapping
    fun getUserLogoutLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ): PagedResponse<UserLogoutLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUserUuid(id).get()
        val logs = userLogoutLogRepository.findByUser(user.id, pr)
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
     * Get user logout logs per username
     */
    @QueryMapping
    fun getUserLogoutLogsByUsername(
        @Argument page: Int,
        @Argument size: Int,
        @Argument username: String
    ): PagedResponse<UserLogoutLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUsername(username).get()
        val logs = userLogoutLogRepository.findByUser(user.id, pr)
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