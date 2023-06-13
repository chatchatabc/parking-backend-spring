package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserLogoutLogGQLController(
    private val userLogoutLogRepository: UserLogoutLogRepository,
) {
    /**
     * Get user logout logs
     */
    @QueryMapping
    fun getUserLogoutLogs(
        @Argument page: Int,
        @Argument size: Int
    ) = run {
        val pr = PageRequest.of(page, size)
        userLogoutLogRepository.findAll(pr).toPagedResponse()
    }

    /**
     * Get user logout logs by user identifier
     */
    @QueryMapping
    fun getUserLogoutLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val pr = PageRequest.of(page, size)
        userLogoutLogRepository.findByUser(id.user.id, pr).toPagedResponse()
    }
}