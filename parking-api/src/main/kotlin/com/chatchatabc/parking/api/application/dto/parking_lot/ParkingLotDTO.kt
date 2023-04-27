package com.chatchatabc.parking.api.application.dto.parking_lot

data class ParkingLotCreateRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val description: String,
    val capacity: Int,
)

data class ParkingLotUpdateRequest(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val description: String?,
    val capacity: Int?,
    val availableSlots: Int?
)
