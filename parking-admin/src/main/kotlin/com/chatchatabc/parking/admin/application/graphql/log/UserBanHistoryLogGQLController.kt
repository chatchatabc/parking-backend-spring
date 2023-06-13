package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserBanHistoryLogGQLController(
    private val userBanHistoryLogRepository: UserBanHistoryLogRepository
) {
    /**
     * Get Ban History Logs by User Identifier
     */
    @QueryMapping
    fun getBanHistoryLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ) = run {
        val pr = PageRequest.of(page, size)
        userBanHistoryLogRepository.findAllByUser(id.user.id, pr).toPagedResponse()
    }

    /**
     * Get All Ban History Logs
     */
    @QueryMapping
    fun getAllBanHistoryLogs(
        @Argument page: Int,
        @Argument size: Int
    ) = run {
        val pr = PageRequest.of(page, size)
        userBanHistoryLogRepository.findAll(pr).toPagedResponse()
    }
}