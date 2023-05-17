package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.*
import com.chatchatabc.parking.admin.application.mapper.MemberMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.log.MemberBanHistoryLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.repository.log.MemberBanHistoryLogRepository
import com.chatchatabc.parking.domain.service.MemberService
import com.chatchatabc.parking.web.common.application.common.MemberPrincipal
import org.mapstruct.factory.Mappers
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
    private val passwordEncoder: PasswordEncoder,
    private val memberBanHistoryLogRepository: MemberBanHistoryLogRepository
) {
    private val memberMapper = Mappers.getMapper(MemberMapper::class.java)

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
     * Update Member Request
     */
    data class MemberUpdateRequest(
        val email: String?,
        val username: String?,
        val firstName: String?,
        val lastName: String?,
    )

    /**
     * Update member
     */
    @PutMapping("/update/{memberUuid}")
    fun updateMember(
        @RequestBody req: MemberUpdateRequest,
        @PathVariable memberUuid: String
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberUuid(memberUuid).get()
            memberMapper.updateMemberFromUpdateRequest(req, member)
            memberService.updateMember(member)
            return ResponseEntity.ok(
                ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
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

    // TODO: Implement update phone number api

    /**
     * Override member password
     */
    @PutMapping("/override-password/{memberUuid}")
    fun overrideMemberPassword(
        @PathVariable memberUuid: String,
        @RequestBody req: MemberOverridePasswordRequest
    ): ResponseEntity<ApiResponse<Member>> {
        return try {
            val member = memberRepository.findByMemberUuid(memberUuid).get().apply {
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

    /**
     * Ban member
     */
    @PostMapping("/ban/{memberUuid}")
    fun banMember(
        @PathVariable memberUuid: String,
        @RequestBody req: MemberBanRequest,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<MemberBanHistoryLog>> {
        return try {
            val bannedBy = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val member = memberRepository.findByMemberUuid(memberUuid).get()
            val banLog = MemberBanHistoryLog().apply {
                this.member = member.id
                this.bannedBy = bannedBy.id
                this.reason = req.reason
                this.until = req.until
                this.status = 0
            }
            ResponseEntity.ok().body(
                ApiResponse(
                    memberBanHistoryLogRepository.save(banLog), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
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

    /**
     * Unban member
     */
    @PostMapping("/unban/{memberUuid}")
    fun unbanMember(
        @PathVariable memberUuid: String,
        @RequestBody req: MemberUnbanRequest,
        principal: MemberPrincipal
    ): ResponseEntity<ApiResponse<MemberBanHistoryLog>> {
        return try {
            val unbannedBy = memberRepository.findByMemberUuid(principal.memberUuid).get()
            val member = memberRepository.findByMemberUuid(memberUuid).get()
            // Get latest ban log
            val banLog = memberBanHistoryLogRepository.findLatestBanLog(member).get().apply {
                this.unbannedBy = unbannedBy.id
                this.unbanReason = req.unbanReason
                this.status = -1
            }
            ResponseEntity.ok().body(
                ApiResponse(
                    memberBanHistoryLogRepository.save(banLog), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
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
}