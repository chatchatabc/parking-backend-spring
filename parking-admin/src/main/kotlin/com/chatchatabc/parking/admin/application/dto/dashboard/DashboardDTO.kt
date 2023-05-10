package com.chatchatabc.parking.admin.application.dto.dashboard

data class DashboardStatistics(
    val totalMembers: Long?,
    val totalVerifiedMembers: Long?,
    val totalUnverifiedMembers: Long?,
    val totalVehicles: Long?,
    val totalParkingLots: Long?,
    val totalVerifiedParkingLots: Long?,
    val totalUnverifiedParkingLots: Long?,
    val totalActiveInvoices: Long?
)
