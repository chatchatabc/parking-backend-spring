package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.domain.model.User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
        private val httpSession: HttpSession
) {

    /**
     * Logout user
     */
    @PostMapping("/logout")
    fun logoutUser(
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val authentication: Authentication? = SecurityContextHolder.getContext().authentication
            if (authentication == null) {
                throw Exception("User not logged in")
            }
            httpSession.invalidate()
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), "Logout successful", false))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), e.message, true))
        }
    }
}