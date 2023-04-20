package com.chatchatabc.parking.application.dto

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.Report

data class ReportCreateRequest(
    val name: String,
    val description: String,
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
)

data class ReportResponse(
    val report: Report?,
    val errorContent: ErrorContent?
)
