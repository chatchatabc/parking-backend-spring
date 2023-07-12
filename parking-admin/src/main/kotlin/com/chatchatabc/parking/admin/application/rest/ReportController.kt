package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.model.ReportStatus
import com.chatchatabc.parking.domain.report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.domain.repository.ReportStatusRepository
import com.chatchatabc.parking.domain.service.ReportService
import com.chatchatabc.parking.domain.service.ReportStatusService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/report")
class ReportController(
    private val reportService: ReportService,
    private val reportStatusService: ReportStatusService,
    private val reportRepository: ReportRepository,
    private val reportStatusRepository: ReportStatusRepository
) {
    /**
     * Get Reports
     */
    @Operation(
        summary = "Get Reports",
        description = "Get Reports"
    )
    @GetMapping
    fun getReports(pageable: Pageable) = runCatching { reportRepository.findAll(pageable).toResponse() }

    /**
     * Get Report By ID
     */
    @Operation(
        summary = "Get Report By ID",
        description = "Get Report By ID"
    )
    @GetMapping("/{id}")
    fun getReport(
        @PathVariable id: Long
    ) = runCatching { id.report.toResponse() }

    /**
     * Get Report Status by ID
     */
    @Operation(
        summary = "Get Report Status by ID",
        description = "Get Report Status by ID"
    )
    @GetMapping("/status/{id}")
    fun getReportStatus(
        @PathVariable id: Long,
        pageable: Pageable
    ) = run {
        reportStatusRepository.findAllByReport(id, pageable).toResponse()
    }

    data class ReportStatusCreateRequest(
        val status: Int,
        val remarks: String
    )

    /**
     * Create Report Status
     */
    @Operation(
        summary = "Create Report Status",
        description = "Create Report Status. -1 = Cancelled, 0 = Draft, 1 = Pending, 2 = Rejected, 3 = Ongoing, 4 = Resolved"
    )
    @PostMapping("/status/{id}")
    fun createReportStatus(
        @PathVariable id: Long,
        @RequestBody request: ReportStatusCreateRequest,
        principal: Principal
    ) = runCatching {
        val report = id.report
        report.status = request.status

        val reportStatus = ReportStatus().apply {
            this.status = request.status
            this.remarks = request.remarks
            this.performedBy = principal.name.user.id
            this.report = report.id
        }

        reportService.saveReport(report)
        reportStatusService.saveReportStatus(reportStatus)
    }
}