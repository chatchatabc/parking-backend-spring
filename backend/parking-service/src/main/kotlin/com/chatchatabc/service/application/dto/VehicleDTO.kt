package com.chatchatabc.service.application.dto

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.service.domain.model.Vehicle

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
