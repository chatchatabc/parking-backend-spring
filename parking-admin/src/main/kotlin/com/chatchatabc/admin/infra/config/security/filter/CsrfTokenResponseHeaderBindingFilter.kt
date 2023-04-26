package com.chatchatabc.admin.infra.config.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CsrfTokenResponseHeaderBindingFilter : OncePerRequestFilter() {
    protected val REQUEST_ATTRIBUTE_NAME = "_csrf"
    protected val RESPONSE_HEADER_NAME = "X-CSRF-HEADER"
    protected val RESPONSE_PARAM_NAME = "X-CSRF-PARAM"
    protected val RESPONSE_TOKEN_NAME = "X-CSRF-TOKEN"

    /**
     * Filter the Session Token
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val token = request.getAttribute(REQUEST_ATTRIBUTE_NAME)
        if (token != null) {
            val csrfToken = token as CsrfToken
            response.setHeader(RESPONSE_HEADER_NAME, csrfToken.headerName)
            response.setHeader(RESPONSE_PARAM_NAME, csrfToken.parameterName)
            response.setHeader(RESPONSE_TOKEN_NAME, csrfToken.token)
        }

        filterChain.doFilter(request, response)
    }
}