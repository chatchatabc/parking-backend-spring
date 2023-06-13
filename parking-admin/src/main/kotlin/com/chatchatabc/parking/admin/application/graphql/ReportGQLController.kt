package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.model.Report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.web.common.PageInfo
import com.chatchatabc.parking.web.common.PagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

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
    ): PagedResponse<Report> {
        val pr = PageRequest.of(page, size)
        val reports = reportRepository.findAll(pr)
        return PagedResponse(
            reports.content,
            PageInfo(
                reports.size,
                reports.totalElements,
                reports.isFirst,
                reports.isLast,
                reports.isEmpty
            )
        )
    }

    /**
     * Get report by id
     */
    @QueryMapping
    fun getReportById(
        @Argument id: Long
    ): Optional<Report> {
        return reportRepository.findById(id)
    }
}