package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.UserBanHistoryLog
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserBanHistoryLogGQLController(
    private val userRepository: UserRepository,
    private val userBanHistoryLogRepository: UserBanHistoryLogRepository
) {
    /**
     * Get Ban History Logs by User
     */
    @QueryMapping
    fun getBanHistoryLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): PagedResponse<UserBanHistoryLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUserUuid(uuid).get()
        val logs = userBanHistoryLogRepository.findAllByUser(user.id, pr)
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
     * Get All Ban History Logs
     */
    @QueryMapping
    fun getAllBanHistoryLogs(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<UserBanHistoryLog> {
        val pr = PageRequest.of(page, size)
        val logs = userBanHistoryLogRepository.findAll(pr)
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
     * Get Ban History Logs by Username
     */
    @QueryMapping
    fun getBanHistoryLogsByUsername(
        @Argument page: Int,
        @Argument size: Int,
        @Argument username: String
    ): PagedResponse<UserBanHistoryLog> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        val user = userRepository.findByUsername(username).get()
        val logs = userBanHistoryLogRepository.findAllByUser(user.id, pr)
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
     * Get Ban History Logs by Phone
     */
    @QueryMapping
    fun getBanHistoryLogsByPhone(
        @Argument page: Int,
        @Argument size: Int,
        @Argument phone: String
    ): PagedResponse<UserBanHistoryLog> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByPhone(phone).get()
        val logs = userBanHistoryLogRepository.findAllByUser(user.id, pr)
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