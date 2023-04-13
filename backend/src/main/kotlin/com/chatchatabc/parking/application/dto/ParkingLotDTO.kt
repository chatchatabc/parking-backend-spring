package com.chatchatabc.parking.application.dto

import com.chatchatabc.parking.domain.model.ParkingLot
import java.math.BigDecimal

data class ParkingLotCreateRequest(
    val name: String,
    val rate: BigDecimal,
    val location: String,
    val capacity: Int
)

data class ParkingLotUpdateRequest(
    val name: String,
    val rate: BigDecimal,
    val location: String,
    val capacity: Int
)

data class ParkingLotResponse(
    val parkingLot: ParkingLot?,
    val error: ErrorContent?
)
