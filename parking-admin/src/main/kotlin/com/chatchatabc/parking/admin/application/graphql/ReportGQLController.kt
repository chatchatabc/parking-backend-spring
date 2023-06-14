package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ReportGQLController(
    private val reportRepository: ReportRepository
) {
    /**
     * Get reports
     */
    @QueryMapping
    fun getReports(
        @Argument page: Int,
        @Argument size: Int
    ) = run {
        val pr = PageRequest.of(page, size)
        reportRepository.findAll(pr).toPagedResponse()
    }

    /**
     * Get report by id
     */
    @QueryMapping
    fun getReportById(@Argument id: Long) = run { id.report }
}