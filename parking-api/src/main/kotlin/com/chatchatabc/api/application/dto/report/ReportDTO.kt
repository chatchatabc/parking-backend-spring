package com.chatchatabc.api.application.dto.report

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.Report

data class ReportResponse(
    val report: Report?,
    val error: ErrorContent?
)

data class ReportCreateRequest(
    val name: String,
    val description: String,
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
)