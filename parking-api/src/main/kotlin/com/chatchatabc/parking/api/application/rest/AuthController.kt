package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.member.MemberPhoneLoginRequest
import com.chatchatabc.parking.api.application.dto.member.MemberVerifyOTPRequest
import com.chatchatabc.parking.api.application.event.member.MemberLoginEvent
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.enums.RoleNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.service.MemberService
import com.chatchatabc.parking.web.common.application.rest.service.JwtService
import io.swagger.v3.oas.annotations.Operation
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
    private val applicationEventPublisher: ApplicationEventPublisher
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
        @RequestBody request: MemberVerifyOTPRequest,
        @PathVariable type: String,
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val headers = HttpHeaders()
            var roleName: RoleNames = RoleNames.ROLE_MEMBER
            if (type == "owner") {
                roleName = RoleNames.ROLE_PARKING_OWNER
            }
            val member = memberService.verifyOTPAndAddRole(request.phone, request.otp, roleName)
            val token: String = jwtService.generateToken(member.memberId)
            headers.set("X-Access-Token", token)
            ResponseEntity.ok().headers(headers).body(
                ApiResponse(member, HttpStatus.OK.value(), ResponseNames.MEMBER_VERIFY_OTP_SUCCESS.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
                )
        }
    }
}