package com.chatchatabc.api.application.dto.parking_lot

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.parking.domain.model.ParkingLot

data class ParkingLotResponse(
    val parkingLot: ParkingLot?,
    val error: ErrorContent?
)

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
