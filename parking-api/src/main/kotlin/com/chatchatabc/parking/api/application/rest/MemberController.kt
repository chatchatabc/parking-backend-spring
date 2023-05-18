package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.MemberNotificationResponse
import com.chatchatabc.parking.api.application.mapper.MemberMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.service.MemberService
import com.chatchatabc.parking.infra.service.FileStorageService
import com.chatchatabc.parking.web.common.application.common.MemberPrincipal
import io.swagger.v3.oas.annotations.Operation
import jakarta.servlet.http.HttpServletResponse
import org.mapstruct.factory.Mappers
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val fileStorageService: FileStorageService,
) {
    private val memberMapper = Mappers.getMapper(MemberMapper::class.java)

    /**
     * Get member profile
     */
    @Operation(
        summary = "Get the profile of the logged in member",
        description = "Member to get the profile of the logged in member."
    )
    @GetMapping("/me")
    fun getProfile(
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
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
    fun getNotificationId(
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<MemberNotificationResponse>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            ResponseEntity.ok().body(
                ApiResponse(
                    MemberNotificationResponse(member.notificationUuid),
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

    data class MemberProfileUpdateRequest(
        val username: String?,
        val email: String?,
        val firstName: String?,
        val lastName: String?,
    )

    /**
     * Update member
     */
    @Operation(
        summary = "Update the details of a member's profile",
        description = "Member to update the details of a member's profile."
    )
    @PutMapping("/update")
    fun updateMember(
        @RequestBody request: MemberProfileUpdateRequest,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            memberMapper.updateMemberFromUpdateProfileRequest(request, member)
            memberService.updateMember(member)
            ResponseEntity.ok().body(
                ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true)
                )
        }
    }

    // TODO: Implement update phone number api

    /**
     * Upload member avatar
     */
    @PostMapping("/upload-avatar")
    fun uploadAvatar(
        @RequestParam("file", required = true) file: MultipartFile,
        principal: MemberPrincipal
    ): ApiResponse<Member> {
        return try {
            val member = memberRepository.findByMemberUuid(principal.memberUuid).get()
            memberService.uploadImage(
                member,
                "avatar",
                file.inputStream,
                file.originalFilename,
                file.size,
                file.contentType
            )
            ApiResponse(member, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true)
        }
    }

    /**
     * Get member avatar by username
     */
    @GetMapping("/avatar/{username}")
    fun getMemberAvatar(
        @PathVariable username: String,
        response: HttpServletResponse
    ): ResponseEntity<InputStreamResource> {
        return try {
            val member = memberRepository.findByUsername(username).get()
            if (member.avatar == null) {
                throw Exception("Avatar not found")
            }
            val headers = HttpHeaders()
            val resource = InputStreamResource(fileStorageService.downloadFile(member.avatar.key))
            ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
        }
    }

    /**
     * Get member avatar by memberId
     */
    @GetMapping("/avatar/id/{memberUuid}")
    fun getMemberAvatarById(
        @PathVariable memberUuid: String,
        response: HttpServletResponse
    ): ResponseEntity<InputStreamResource> {
        return try {
            val member = memberRepository.findByMemberUuid(memberUuid).get()
            if (member.avatar == null) {
                throw Exception("Avatar not found")
            }
            val headers = HttpHeaders()
            val resource = InputStreamResource(fileStorageService.downloadFile(member.avatar.key))
            ResponseEntity<InputStreamResource>(resource, headers, HttpStatus.OK)
        } catch (e: Exception) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
            ResponseEntity<InputStreamResource>(HttpStatus.NOT_FOUND)
        }
    }
}