package com.chatchatabc.admin.infra.config.security.filter

import com.chatchatabc.parking.domain.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SessionRequestFilter(
        private val httpSession: HttpSession,
        private val userRepository: UserRepository
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(SessionRequestFilter::class.java)

    /**
     * Filter the JWT token and validate
     */
    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain) {
        // Get User ID from session
        // TODO: verify, this might not be safe
        val userId = httpSession.getAttribute("userId") as String
        if (userId == null) {
            filterChain.doFilter(request, response)
            logRequest(request, response)
            return
        }

        val user = userRepository.findById(userId).get()

        val authentication = UsernamePasswordAuthenticationToken(
                user, null, user.authorities
        )

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
        logRequest(request, response, user.id)
    }

    /**
     * Log the request path and the response code
     */
    fun logRequest(request: HttpServletRequest, response: HttpServletResponse) {
        // Log the path of the request
        log.info("Request path: ${request.method} ${request.requestURL} from ${request.remoteAddr} with code ${response.status}")
    }

    /**
     * Log the request path and the response code with user id
     */
    fun logRequest(request: HttpServletRequest, response: HttpServletResponse, userId: String) {
        // Log the path of the request
        log.info("Request path: ${request.method} ${request.requestURL} User ID $userId from ${request.remoteAddr} with code ${response.status}")
    }
}