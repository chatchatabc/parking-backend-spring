package com.chatchatabc.service.application.dto

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.service.domain.model.ParkingLot
import java.math.BigDecimal

data class ParkingLotCreateRequest(
    val name: String,
    val rate: BigDecimal,
    val latitude: Double,
    val longitude: Double,
    val capacity: Int
)

data class ParkingLotUpdateRequest(
    val name: String?,
    val rate: BigDecimal?,
    val latitude: Double?,
    val longitude: Double?,
    val capacity: Int?
)

data class ParkingLotResponse(
    val parkingLot: ParkingLot?,
    val error: ErrorContent?
)
