package com.chatchatabc.parking.admin.application.graphql.log

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.log.MemberLoginLog
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.log.MemberLoginLogRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class MemberLoginLogGQLController(
    private val memberLoginLogRepository: MemberLoginLogRepository,
    private val memberRepository: MemberRepository
) {
    /**
     * Get user login logs
     */
    @QueryMapping
    fun getUserLoginLogs(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<MemberLoginLog> {
        val pr = PageRequest.of(page, size)
        val logs = memberLoginLogRepository.findAll(pr)
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
     * Get user login logs per user
     */
    @QueryMapping
    fun getUserLoginLogsByUser(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ): PagedResponse<MemberLoginLog> {
        val pr = PageRequest.of(page, size)
        val user = memberRepository.findByMemberId(id).get()
        val logs = memberLoginLogRepository.findByMember(user, pr)
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