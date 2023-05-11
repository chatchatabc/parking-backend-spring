package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Member
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.specification.MemberSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class MemberGQLController(
    private val memberRepository: MemberRepository,
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
}