package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import com.chatchatabc.parking.user
import com.chatchatabc.parking.web.common.PageInfo
import com.chatchatabc.parking.web.common.PagedResponse
import com.chatchatabc.parking.web.common.toPagedResponse
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