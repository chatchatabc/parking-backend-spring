package com.chatchatabc.parking.api.application.dto.parking_lot

import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import java.time.LocalDateTime

data class ParkingLotCreateRequest(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val description: String,
    val capacity: Int,
    val businessHoursStart: LocalDateTime?,
    val businessHoursEnd: LocalDateTime?,
    val openDaysFlag: Int = 0
)

data class ParkingLotUpdateRequest(
    val name: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val description: String?,
    val capacity: Int?,
    val availableSlots: Int?,
    val businessHoursStart: LocalDateTime?,
    val businessHoursEnd: LocalDateTime?,
    val openDaysFlag: Int?,
    val images: List<ParkingLotImage>?
)