package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.service.log.MemberLogoutLogService
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
    private val memberRepository: MemberRepository,
    private val memberLogoutLogService: MemberLogoutLogService
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
        request: HttpServletRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.name).get()
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
        response: HttpServletResponse,
        principal: Principal
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.name).get()
            memberLogoutLogService.createLog(member.id, 1, request.remoteAddr)
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true))
        }
    }
}