package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.ReportMapper
import com.chatchatabc.parking.domain.model.Report
import com.chatchatabc.parking.domain.report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.domain.repository.ReportStatusRepository
import com.chatchatabc.parking.domain.service.ReportService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/report")
class ReportController(
    private val reportRepository: ReportRepository,
    private val reportService: ReportService,
    private val reportStatusRepository: ReportStatusRepository
) {
    private val reportMapper = Mappers.getMapper(ReportMapper::class.java)

    /**
     * Get Reports
     */
    @Operation(
        summary = "Get Reports",
        description = "Get Reports"
    )
    @GetMapping
    fun getReports(
        pageable: Pageable
    ) = runCatching {
        // TODO: Get user from security context
        reportRepository.findAll(pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get Report by Identifier
     */
    @Operation(
        summary = "Get Report by Identifier",
        description = "Get Report by Identifier"
    )
    @GetMapping("/{id}")
    fun getReport(
        @PathVariable id: Long
    ) = runCatching { id.report.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Get Report Status
     */
    @Operation(
        summary = "Get Report Status",
        description = "Get Report Status"
    )
    @GetMapping("/status/{id}")
    fun getReportStatus(
        @PathVariable id: Long,
        pageable: Pageable
    ) = runCatching {
        reportStatusRepository.findAllByReport(id, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get reports by Reported By
     */
    @Operation(
        summary = "Get reports by Reported By",
        description = "Get reports by Reported By"
    )
    @GetMapping("/reporter/{userUuid}")
    fun getReportsBy(
        @PathVariable userUuid: String,
        pageable: Pageable
    ) = runCatching {
        reportRepository.findAllByReportedBy(userUuid.user.id, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Create Report
     */
    @Operation(
        summary = "Create Report",
        description = "Create Report"
    )
    @PostMapping
    fun createReport(
        @RequestBody req: ReportMapper.ReportMapDTO
    ) = runCatching {
        val report = Report()
        reportMapper.mapRequestToReport(req, report)
        reportService.saveReport(report).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Report
     */
    @Operation(
        summary = "Update Report",
        description = "Update Report"
    )
    @PutMapping("/{reportId}")
    fun updateReport(
        @PathVariable reportId: Long,
        @RequestBody req: ReportMapper.ReportMapDTO
    ) = runCatching {
        val report = reportId.report
        if (report.status != Report.ReportStatus.DRAFT) {
            throw Exception("Report no longer in draft status")
        }
        reportMapper.mapRequestToReport(req, report)
        reportService.saveReport(report).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Set Report to Pending
     */
    @Operation(
        summary = "Set Report to Pending",
        description = "Set Report to Pending"
    )
    @PutMapping("/set-pending/{id}")
    fun setReportToPending(
        @PathVariable id: Long
    ) = runCatching {
        val report = id.report
        if (report.status != Report.ReportStatus.DRAFT) {
            throw Exception("Report no longer in draft status")
        }
        report.apply { status = Report.ReportStatus.PENDING }
        reportService.saveReport(report).toResponse()
    }.getOrElse { it.toErrorResponse() }
}