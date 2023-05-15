package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.log.MemberLogoutLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.log.MemberLogoutLogRepository
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val memberRepository: MemberRepository,
    private val memberLogoutLogRepository: MemberLogoutLogRepository
) {
    /**
     * Get Member Profile
     */
    @Operation(
        summary = "Get the profile of the logged in member",
        description = "Member to get the profile of the logged in member."
    )
    @GetMapping("/me")
    fun getProfile(
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            ResponseEntity.ok().body(
                ApiResponse(member, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR.name,
                        true
                    )
                )
        }
    }

    /**
     * Logout member
     */
    @PostMapping("/logout")
    fun logoutMember(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            memberLogoutLogRepository.save(
                MemberLogoutLog().apply {
                    this.member = member
                    this.type = 1
                    this.ipAddress = request.remoteAddr
                }
            )
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true))
        }
    }
}