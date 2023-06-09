package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.api.application.mapper.ReportMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Report
import com.chatchatabc.parking.domain.repository.ReportRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.ReportService
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/report")
class ReportController(
    private val reportRepository: ReportRepository,
    private val reportService: ReportService,
    private val userRepository: UserRepository
) {
    private val reportMapper = Mappers.getMapper(ReportMapper::class.java)

    /**
     * Get Reports
     */
    @GetMapping("/reports")
    fun getReports(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Report>>> {
        return try {
            // Get user from security context
            val reports = reportRepository.findAll(pageable)
            ResponseEntity.ok(ApiResponse(reports, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Get reports by Reported By
     */
    @GetMapping("/reported-by/{userUuid}")
    fun getReportsBy(
        @PathVariable userUuid: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Report>>> {
        return try {
            val user = userRepository.findByUserUuid(userUuid).orElseThrow()
            val reports = reportRepository.findAllByReportedBy(user.id, pageable)
            ResponseEntity.ok(ApiResponse(reports, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

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
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val report = Report()
            reportMapper.createReportFromRequest(req, report)
            reportService.saveReport(report)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

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
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val report = reportRepository.findById(reportId).orElseThrow()
            reportMapper.updateReportFromRequest(req, report)
            reportRepository.save(report)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }
}