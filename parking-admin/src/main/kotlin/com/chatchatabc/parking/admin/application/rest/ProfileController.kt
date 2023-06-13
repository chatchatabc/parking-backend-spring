package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.service.log.UserLogoutLogService
import com.chatchatabc.parking.user
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
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
     * Get User Profile
     */
    @Operation(
        summary = "Get the profile of the logged in user",
        description = "User to get the profile of the logged in user."
    )
    @GetMapping("/me")
    fun getProfile(
        request: HttpServletRequest,
        principal: Principal
    ) = runCatching { principal.name.user.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    fun logoutUser(
        request: HttpServletRequest,
        response: HttpServletResponse,
        principal: Principal
    ) = runCatching {
        userLogoutLogService.createLog(principal.name.user.id, 1, request.remoteAddr).toResponse()
    }.getOrElse { it.toErrorResponse() }
}