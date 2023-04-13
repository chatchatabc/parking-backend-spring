package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.Vehicle

data class VehicleRegisterRequest(
    val plateNumber: String
)

data class VehicleUpdateRequest(
    val plateNumber: String?
)

data class VehicleResponse(
    val vehicle: Vehicle?,
    val error: ErrorContent?
)
