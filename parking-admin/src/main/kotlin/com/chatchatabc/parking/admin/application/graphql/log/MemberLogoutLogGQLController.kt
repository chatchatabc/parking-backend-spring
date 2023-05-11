package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.MemberLogoutLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.log.MemberLogoutLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MemberLogoutLogGQLController(
    private val memberLogoutLogRepository: MemberLogoutLogRepository,
    private val memberRepository: MemberRepository
) {
    /**
     * Get user logout logs
     */
    @QueryMapping
    fun getUserLogoutLogs(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<MemberLogoutLog> {
        val pr = PageRequest.of(page, size)
        val logs = memberLogoutLogRepository.findAll(pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }

    /**
     * Get user logout logs per user
     */
    @QueryMapping
    fun getUserLogoutLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ): PagedResponse<MemberLogoutLog> {
        val pr = PageRequest.of(page, size)
        val user = memberRepository.findByMemberUuid(id).get()
        val logs = memberLogoutLogRepository.findByMember(user, pr)
        return PagedResponse(
            logs.content,
            PageInfo(
                logs.size,
                logs.totalElements,
                logs.isFirst,
                logs.isLast,
                logs.isEmpty
            )
        )
    }
}