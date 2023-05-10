package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.member.MemberCreateRequest
import com.chatchatabc.parking.admin.application.dto.member.MemberOverridePasswordRequest
import com.chatchatabc.parking.admin.application.dto.member.MemberUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
class MemberController(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * Create member
     */
    @PostMapping("/create")
    fun createMember(
        @RequestBody req: MemberCreateRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val roles = roleRepository.findRolesIn(req.roles)
            val member = Member().apply {
                this.phone = req.phone
                this.username = req.username
                this.email = req.email
                this.roles = roles
                if (req.enabled) {
                    this.status = 0
                } else {
                    this.status = 1
                }
            }
            val createdMember = memberRepository.save(member)
            return ResponseEntity.ok(
                ApiResponse(
                    createdMember, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                    )
                )
        }
    }

    /**
     * Update member
     */
    @PutMapping("/update/{id}")
    fun updateMember(
        @RequestBody req: MemberUpdateRequest, @PathVariable id: String
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val updatedMember = memberService.updateMember(
                id,
                req.phone,
                req.username,
                req.email,
                req.firstName,
                req.lastName
            )
            return ResponseEntity.ok(
                ApiResponse(
                    updatedMember, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                    )
                )
        }
    }

    /**
     * Override member password
     */
    @PutMapping("/override-password/{memberId}")
    fun overrideMemberPassword(
        @PathVariable memberId: String,
        @RequestBody req: MemberOverridePasswordRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberId(memberId).get().apply {
                this.password = passwordEncoder.encode(req.newPassword)
            }
            return ResponseEntity.ok(
                ApiResponse(
                    memberRepository.save(member), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                )
            )
        }
    }

    // TODO: Ban member
}