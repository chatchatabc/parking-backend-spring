package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.UserLogoutLog
import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserLogoutLogResolver(
    private val userLogoutLogRepository: UserLogoutLogRepository
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
}