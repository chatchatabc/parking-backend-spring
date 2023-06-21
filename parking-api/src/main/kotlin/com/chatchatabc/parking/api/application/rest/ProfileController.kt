package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.service.log.UserLogoutLogService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val userLogoutLogService: UserLogoutLogService
) {
    /**
     * Logout user
     */
    @Operation(
        summary = "Logout user",
        description = "Logout user"
    )
    @PostMapping("/logout")
    fun logoutUser(
        request: HttpServletRequest,
        response: HttpServletResponse,
        principal: Principal
    ) = runCatching {
        userLogoutLogService.createLog(principal.name.user.id, 1, request.remoteAddr).toResponse()
    }.getOrElse { it.toErrorResponse() }
}