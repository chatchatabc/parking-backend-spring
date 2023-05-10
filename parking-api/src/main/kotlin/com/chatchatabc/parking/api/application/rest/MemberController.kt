package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.member.MemberNotificationResponse
import com.chatchatabc.parking.api.application.dto.member.MemberProfileUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.service.MemberService
import com.chatchatabc.parking.domain.service.service.CloudFileService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
) : CloudFileService() {

    /**
     * Get member profile
     */
    @Operation(
        summary = "Get the profile of the logged in member",
        description = "Member to get the profile of the logged in member."
    )
    @GetMapping("/me")
    fun getProfile(): ResponseEntity<ApiResponse<Member>> {
        return try {
            // Get ID from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberRepository.findById(principal.id).get()
            ResponseEntity.ok().body(
                ApiResponse(
                    member, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
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
     * Get member notification id
     */
    @Operation(
        summary = "Get the notification id of the logged in member",
        description = "Get notification id of the logged in member. This is used for push notifications and should not be available to other members."
    )
    @GetMapping("/get-notification-id")
    fun getNotificationId(): ResponseEntity<ApiResponse<MemberNotificationResponse>> {
        return try {
            // Get member from security context holder
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberRepository.findById(principal.id).get()
            ResponseEntity.ok().body(
                ApiResponse(
                    MemberNotificationResponse(member.notificationId),
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
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
     * Update member
     */
    @Operation(
        summary = "Update the details of a member's profile",
        description = "Member to update the details of a member's profile."
    )
    @PutMapping("/update")
    fun updateMember(
        @RequestBody request: MemberProfileUpdateRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            // Get principal from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberService.updateMember(
                principal.memberId,
                request.phone,
                request.username,
                request.email,
                request.firstName,
                request.lastName
            )
            ResponseEntity.ok().body(
                ApiResponse(member, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true)
                )
        }
    }

    /**
     * Upload member avatar
     */
    @PostMapping("/upload-avatar")
    fun uploadAvatar(
        @RequestParam("file", required = true) file: MultipartFile
    ): ApiResponse<Member> {
        return try {
            // Get principal from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val member = memberRepository.findById(principal.id).get()
            memberService.uploadImage(member, "avatar", file)
            ApiResponse(member, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
        }
    }
}