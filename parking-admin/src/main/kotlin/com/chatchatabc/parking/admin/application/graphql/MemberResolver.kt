package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.model.log.MemberLogoutLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.log.MemberLogoutLogRepository
import com.chatchatabc.parking.domain.specification.MemberSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class MemberResolver(
    private val memberRepository: MemberRepository,
    private val memberLogoutLogRepository: MemberLogoutLogRepository,
) {

    /**
     * Get member by id
     */
    @QueryMapping
    fun getMemberById(
        @Argument id: String
    ): Optional<Member> {
        return memberRepository.findByMemberId(id)
    }

    /**
     * Get member by username
     */
    @QueryMapping
    fun getMemberByUsername(
        @Argument username: String
    ): Optional<Member> {
        return memberRepository.findByUsername(username)
    }

    /**
     * Get member by phone
     */
    @QueryMapping
    fun getMemberByPhone(
        @Argument phone: String
    ): Optional<Member> {
        return memberRepository.findByPhone(phone)
    }

    /**
     * Get all members w/ keyword
     */
    @QueryMapping
    fun getMembers(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?
    ): PagedResponse<Member> {
        val pr = PageRequest.of(page, size)
        val spec = MemberSpecification.withKeyword(keyword ?: "")
        val members = memberRepository.findAll(spec, pr)
        return PagedResponse(
            members.content,
            PageInfo(
                members.size,
                members.totalElements,
                members.isFirst,
                members.isLast,
                members.isEmpty
            )
        )
    }

    /**
     * Get Member Logout Logs w/ pageable
     */
    // TODO: Add date range?
    @GetMapping("/get-logout-logs")
    fun getMemberLogoutLogs(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<MemberLogoutLog>>> {
        return try {
            val logs = memberLogoutLogRepository.findAll(pageable)
            ResponseEntity.ok(
                ApiResponse(
                    logs,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }
}