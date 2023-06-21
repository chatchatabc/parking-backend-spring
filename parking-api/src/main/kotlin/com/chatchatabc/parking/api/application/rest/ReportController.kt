package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.ReportMapper
import com.chatchatabc.parking.domain.model.Report
import com.chatchatabc.parking.domain.report
import com.chatchatabc.parking.domain.repository.ReportRepository
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
) {
    private val reportMapper = Mappers.getMapper(ReportMapper::class.java)

    /**
     * Get Reports
     */
    @Operation(
        summary = "Get Reports",
        description = "Get Reports"
    )
    @GetMapping("/")
    fun getReports(
        pageable: Pageable
    ) = runCatching {
        // TODO: Get user from security context
        reportRepository.findAll(pageable).toResponse()
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
     * Create Report Data Class
     */
    data class ReportCreateRequest(
        val name: String,
        val description: String,
        val plateNumber: String,
        val latitude: Double,
        val longitude: Double
    )

    /**
     * Create Report
     */
    @Operation(
        summary = "Create Report",
        description = "Create Report"
    )
    @PostMapping("/")
    fun createReport(
        @RequestBody req: ReportCreateRequest
    ) = runCatching {
        val report = Report()
        reportMapper.createReportFromRequest(req, report)
        reportService.saveReport(report).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Report Data Class
     */
    data class ReportUpdateRequest(
        val name: String?,
        val description: String?,
        val plateNumber: String?,
        val latitude: Double?,
        val longitude: Double?
    )

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
        @RequestBody req: ReportUpdateRequest
    ) = runCatching {
        val report = reportId.report
        reportMapper.updateReportFromRequest(req, report)
        reportService.saveReport(report).toResponse()
    }.getOrElse { it.toErrorResponse() }
}