package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.Vehicle

data class VehicleRegisterRequest(
    val name: String,
    val plateNumber: String,
    val type: Int
)

data class VehicleUpdateRequest(
    val name: String?,
    val plateNumber: String?,
    val type: Int?
)

data class VehicleResponse(
    val vehicle: Vehicle?,
    val error: ErrorContent?
)
