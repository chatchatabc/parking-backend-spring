package com.chatchatabc.parking.admin.application.rest.log

import com.chatchatabc.parking.domain.repository.log.UserBanHistoryLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/log/user-ban-history")
class UserBanHistoryController(
    private val userBanHistoryLogRepository: UserBanHistoryLogRepository
) {
    /**
     * Get All Ban History Logs
     */
    @GetMapping
    fun getBanLogs(pageable: Pageable) = run { userBanHistoryLogRepository.findAll(pageable).toResponse() }

    /**
     * Get Ban History Logs by User Identifier
     */
    @GetMapping("/{id}")
    fun getBanLogsByUser(
        @PathVariable id: String,
        pageable: Pageable
    ) = run {
        userBanHistoryLogRepository.findAllByUser(id.user.id, pageable).toResponse()
    }
}