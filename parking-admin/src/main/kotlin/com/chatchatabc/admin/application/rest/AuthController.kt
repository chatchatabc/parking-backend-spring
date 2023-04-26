package com.chatchatabc.admin.application.rest

import com.chatchatabc.admin.application.dto.ApiResponse
import com.chatchatabc.admin.application.dto.user.UserLoginRequest
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val authenticationManager: AuthenticationManager,
        private val userRepository: UserRepository,
        private val httpSession: HttpSession
) {
    /**
     * Get CSRF token for testing
     * TODO: Remove this route on production
     */
    @GetMapping("/get-csrf-token")
    fun test(
            request: HttpServletRequest
    ): String {
        val token: CsrfToken = request.getAttribute("_csrf") as CsrfToken
        println(token.headerName)
        println(token.token)
        return "CSRF Retrieved!"
    }

    /**
     * Login user and authenticate user
     */
    @PostMapping("/login")
    fun loginUser(
            @RequestBody req: UserLoginRequest,
            request: HttpServletRequest
    ): ResponseEntity<ApiResponse<User>> {
        return try {
            val user = userRepository.findByUsername(req.username).get()

            // Authenticate user
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                    req.username,
                    req.password
            ))
            // Add the userId to the session attribute
            httpSession.setAttribute("userId", user.id)
            ResponseEntity.ok(ApiResponse(user, HttpStatus.OK.value(), "Login successful", false))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), e.message, true))
        }
    }
}