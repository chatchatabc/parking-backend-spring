package com.chatchatabc.parking.web.common.application.config.security.filter;

import com.auth0.jwt.interfaces.Payload;
import com.chatchatabc.parking.web.common.application.common.MemberPrincipal;
import com.chatchatabc.parking.web.common.application.rest.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    private final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    /**
     * Filter the request and add the member to the security context if the token is valid
     *
     * @param request     the request
     * @param response    the response
     * @param filterChain the filter chain
     * @throws ServletException if an error occurs
     * @throws IOException      if an error occurs
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // Get authorization header and validate
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            logRequest(request, response);
            return;
        }

        final String token = header.substring(7); // skip "Bearer "
        final Payload payload = jwtService.validateTokenAndGetPayload(token);

        if (payload == null) {
            filterChain.doFilter(request, response);
            logRequest(request, response);
            return;
        }

        String memberUuid = payload.getSubject();
        String username = payload.getClaim("username").asString();
        MemberPrincipal member = MemberPrincipal.of(memberUuid, username);
        String[] roles = payload.getClaim("roles").asArray(String.class);
        List<GrantedAuthority> authorities = Arrays.stream(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member,
                null,
                authorities
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Continue flow with the member in the security context
        filterChain.doFilter(request, response);
        logRequest(request, response, member.getMemberUuid());
    }

    /**
     * Log the request
     *
     * @param request  the request
     * @param response the response
     */
    private void logRequest(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request path: {} {} from {} with code {}",
                request.getMethod(), request.getRequestURL(), request.getRemoteAddr(), response.getStatus());
    }

    /**
     * Log the request
     *
     * @param request  the request
     * @param response the response
     * @param memberId the member id
     */
    private void logRequest(HttpServletRequest request, HttpServletResponse response, String memberId) {
        log.info("Request path: {} {} Member ID {} from {} with code {}",
                request.getMethod(), request.getRequestURL(), memberId, request.getRemoteAddr(), response.getStatus());
    }
}
