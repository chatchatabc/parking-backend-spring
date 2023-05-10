package com.chatchatabc.parking.admin.application.dto.parking_lot

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
