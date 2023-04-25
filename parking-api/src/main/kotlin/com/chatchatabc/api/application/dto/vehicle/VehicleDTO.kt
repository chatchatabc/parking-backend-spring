package com.chatchatabc.api.application.dto.vehicle

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