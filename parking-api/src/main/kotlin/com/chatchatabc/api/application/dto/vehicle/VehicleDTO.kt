package com.chatchatabc.api.application.dto.vehicle

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.Vehicle

data class VehicleResponse(
    val vehicle: Vehicle?,
    val error: ErrorContent?
)

data class VehicleRegisterRequest(
    val name: String,
    val plateNumber: String,
    val type: Int,
)

data class VehicleUpdateRequest(
    val name: String?,
    val plateNumber: String?,
    val type: Int?,
)