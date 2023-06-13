package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.ReportMapper
import com.chatchatabc.parking.domain.model.Report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.domain.service.ReportService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
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
    @GetMapping("/reports")
    fun getReports(
        pageable: Pageable
    ) = runCatching {
        // TODO: Get user from security context
        reportRepository.findAll(pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get reports by Reported By
     */
    @GetMapping("/reported-by/{userUuid}")
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
    @PostMapping("/create-report")
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
    @PutMapping("/update-report/{reportId}")
    fun updateReport(
        @PathVariable reportId: Long,
        @RequestBody req: ReportUpdateRequest
    ) = runCatching {
        val report = reportRepository.findById(reportId).orElseThrow()
        reportMapper.updateReportFromRequest(req, report)
        reportRepository.save(report).toResponse()
    }.getOrElse { it.toErrorResponse() }
}