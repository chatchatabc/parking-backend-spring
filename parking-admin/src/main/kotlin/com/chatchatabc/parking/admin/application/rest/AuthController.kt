package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.MemberLoginRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.log.MemberLoginLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.log.MemberLoginLogRepository
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService,
    private val memberLoginLogRepository: MemberLoginLogRepository
) {
    /**
     * Login member and authenticate member
     */
    @PostMapping("/login")
    fun loginMember(
        @RequestBody req: MemberLoginRequest,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<Member>> {
        val member = memberRepository.findByUsername(req.username)
        return try {
            // Authenticate member
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    req.username,
                    req.password
                )
            )
            // Generate JWT Token
            val headers = HttpHeaders()
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = member.get().roles.stream()
                .map { it.authority }
                .toList()

            val token: String = jwtService.generateToken(member.get().memberUuid, member.get().username, roleStrings)
            headers.set("X-Access-Token", token)
            // Generate Successful Login Log
            memberLoginLogRepository.save(
                MemberLoginLog().apply {
                    this.member = member.get()
                    this.ipAddress = request.remoteAddr
                    this.type = 1
                    this.success = true
                }
            )
            ResponseEntity.ok().headers(headers)
                .body(ApiResponse(member.get(), HttpStatus.OK.value(), ResponseNames.MEMBER_LOGIN_SUCCESS.name, false))
        } catch (e: Exception) {
            // Generate Failed Login Log
            if (member.isPresent) {
                memberLoginLogRepository.save(
                    MemberLoginLog().apply {
                        this.member = member.get()
                        this.ipAddress = request.remoteAddr
                        this.type = 1
                        this.success = false
                    }
                )
            }
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.MEMBER_BAD_CREDENTIALS.name,
                        true
                    )
                )
        }
    }
}