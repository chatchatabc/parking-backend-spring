package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.MemberPhoneLoginRequest
import com.chatchatabc.parking.api.application.dto.MemberVerifyOTPRequest
import com.chatchatabc.parking.api.application.event.member.MemberLoginEvent
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.service.MemberService
import com.chatchatabc.parking.domain.service.log.MemberLoginLogService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val memberService: MemberService,
    private val jwtService: JwtService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val memberLoginLogService: MemberLoginLogService
) {

    /**
     * Login with phone number or username
     */
    @Operation(
        summary = "Login with phone number or username",
        description = "This API is used for both parking managers and members."
    )
    @PostMapping("/login")
    fun loginWithPhone(
        @RequestBody req: MemberPhoneLoginRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            memberService.softRegisterMember(req.phone, req.username)
            // Generate OTP and set to KV store
            val otp = memberService.generateOTPAndSaveToKV(req.phone, 900L)
            // Send OTP to SMS using events
            applicationEventPublisher.publishEvent(MemberLoginEvent(this, req.phone, otp))
            ResponseEntity.ok().body(
                ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }


    /**
     * Verify OTP dynamically members
     */
    @Operation(
        summary = "Verify the OTP of a member logging in.",
        description = "This API is used for both parking owners and members. Type = owner if verifying a parking owner. Type = member if verifying a member."
    )
    @PostMapping("/verify/{type}")
    fun verifyOTP(
        @RequestBody req: MemberVerifyOTPRequest,
        @PathVariable type: String,
        request: HttpServletRequest
    ): ResponseEntity<ApiResponse<Member>> {
        var member: Member? = null
        return try {
            val headers = HttpHeaders()
            var roleName: RoleNames = RoleNames.ROLE_MEMBER
            if (type == "owner") {
                roleName = RoleNames.ROLE_PARKING_OWNER
            }
            member = memberService.verifyOTPAndAddRole(req.phone, req.otp, roleName)
            // Convert granted authority roles to list of string roles
            val roleStrings: List<String> = member.roles.stream()
                .map { it.authority }
                .toList()

            val token: String = jwtService.generateToken(member.memberUuid, member.username, roleStrings)
            headers.set("X-Access-Token", token)
            // Generate Successful Login Log
            memberLoginLogService.createdLog(member, request.remoteAddr, 0, true)
            ResponseEntity.ok().headers(headers).body(
                ApiResponse(member, HttpStatus.OK.value(), ResponseNames.MEMBER_VERIFY_OTP_SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // Generate Failed Login Log
            if (member != null) {
                memberLoginLogService.createdLog(member, request.remoteAddr, 0, false)
            }
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }
}