package com.chatchatabc.parking.api.application.dto.report

data class ReportCreateRequest(
    val name: String,
    val description: String,
    val plateNumber: String,
    val latitude: Double,
    val longitude: Double,
)