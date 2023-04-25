package com.chatchatabc.parkingmanagement.infra.config.security.filter

import com.chatchatabc.parking.application.rest.service.JwtService
import com.chatchatabc.parkingmanagement.infra.config.security.dto.RoleDTOGrantedAuthority
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.dubbo.config.annotation.DubboReference
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter(
    @DubboReference
    private val jwtService: JwtService
) : OncePerRequestFilter() {
    private val log = LoggerFactory.getLogger(JwtRequestFilter::class.java)

    /**
     * Filter the JWT token and validate
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // Get authorization header and validate
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            logRequest(request, response)
            return
        }

        val token: String = header.substring(7)
        val user = jwtService.validateTokenAndGetUser(token)

        if (user == null) {
            filterChain.doFilter(request, response)
            logRequest(request, response)
            return
        }

        val authorities: MutableCollection<out GrantedAuthority> =
            user.roles.stream().map { role -> RoleDTOGrantedAuthority(role) }.toList().toMutableList()

        val authentication = UsernamePasswordAuthenticationToken(
            user,
            null,
            authorities
        )

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        // Continue flow with authenticated user
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