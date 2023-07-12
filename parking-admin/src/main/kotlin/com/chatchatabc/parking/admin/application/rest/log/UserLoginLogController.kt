package com.chatchatabc.parking.admin.application.rest.log

import com.chatchatabc.parking.domain.repository.log.UserLoginLogRepository
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/log/user-login")
class UserLoginLogController(
    private val userLoginLogRepository: UserLoginLogRepository
) {
    /**
     * Get All Logs
     */
    @GetMapping
    fun getLoginLogs(pageable: Pageable) = run { userLoginLogRepository.findAll(pageable).toResponse() }

    /**
     * Get Logs by User
     */
    @GetMapping("/{id}")
    fun getLoginLogsByUser(
        @PathVariable id: String,
        pageable: Pageable
    ) = run { userLoginLogRepository.findByUser(id.user.id, pageable).toResponse() }
}