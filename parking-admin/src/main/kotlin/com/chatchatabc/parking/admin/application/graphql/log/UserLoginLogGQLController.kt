package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserLoginLogGQLController(
    private val userLoginLogRepository: UserLoginLogRepository,
) {
    /**
     * Get user login logs
     */
    @QueryMapping
    fun getUserLoginLogs(
        @Argument page: Int,
        @Argument size: Int
    ) = run {
        val pr = PageRequest.of(page, size)
        userLoginLogRepository.findAll(pr).toPagedResponse()
    }

    /**
     * Get user login logs by identifier
     */
    @QueryMapping
    fun getUserLoginLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val pr = PageRequest.of(page, size)
        userLoginLogRepository.findByUser(id.user.id, pr).toPagedResponse()
    }
}