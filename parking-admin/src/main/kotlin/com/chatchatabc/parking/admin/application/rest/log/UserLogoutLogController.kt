package com.chatchatabc.parking.admin.application.rest.log

import com.chatchatabc.parking.domain.repository.log.UserLogoutLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/log/user-logout")
class UserLogoutLogController(
    private val userLogoutLotRepository: UserLogoutLogRepository
) {
    /**
     * Get All Logs
     */
    @GetMapping
    fun getLogoutLogs(pageable: Pageable) = run { userLogoutLotRepository.findAll(pageable).toResponse() }

    /**
     * Get Logs by User
     */
    @GetMapping("/{id}")
    fun getLogoutLogsByUser(
        @PathVariable id: String,
        pageable: Pageable
    ) = run { userLogoutLotRepository.findByUser(id.user.id, pageable).toResponse() }
}