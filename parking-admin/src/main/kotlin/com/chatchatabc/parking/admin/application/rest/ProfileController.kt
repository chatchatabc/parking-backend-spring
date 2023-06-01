package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.log.UserLogoutLogService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val userRepository: UserRepository,
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
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            ResponseEntity.ok().body(ApiResponse(user, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Logout user
     */
    @PostMapping("/logout")
    fun logoutUser(
        request: HttpServletRequest,
        response: HttpServletResponse,
        principal: Principal
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            userLogoutLogService.createLog(user.id, 1, request.remoteAddr)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }
}