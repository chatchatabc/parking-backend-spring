package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.model.ReportStatus
import com.chatchatabc.parking.domain.report
import com.chatchatabc.parking.domain.service.ReportService
import com.chatchatabc.parking.domain.service.ReportStatusService
import com.chatchatabc.parking.domain.user
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/report")
class ReportController(
    private val reportService: ReportService,
    private val reportStatusService: ReportStatusService
) {
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
        request: ReportStatusCreateRequest,
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