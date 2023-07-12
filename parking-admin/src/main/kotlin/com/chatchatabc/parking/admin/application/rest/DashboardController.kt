package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val userRepository: UserRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val vehicleRepository: VehicleRepository,
    private val invoiceRepository: InvoiceRepository
) {
    /**
     * Dashboard Pie Graph DTO
     */
    data class DashboardPieGraphDTO(
        // Parking Lots
        var parkingLotVerifiedCount: Long = 0,
        var parkingLotUnverifiedCount: Long = 0,

        // Users
        var userVerifiedCount: Long = 0,
        var userUnverifiedCount: Long = 0,

        // Vehicles
        var vehicleVerifiedCount: Long = 0,
        var vehicleUnverifiedCount: Long = 0,
    )

    /**
     * Get Dashboard Pie Graph
     */
    @GetMapping("/pie")
    fun getDashboardPieGraph() = runCatching {
        DashboardPieGraphDTO().apply {
            // Parking
            parkingLotVerifiedCount = parkingLotRepository.countVerified()
            parkingLotUnverifiedCount = parkingLotRepository.count() - parkingLotVerifiedCount

            // Users
            userVerifiedCount = userRepository.countVerified()
            userUnverifiedCount = userRepository.count() - userVerifiedCount

            // Vehicles
            // TODO: implement vehicle verification system
            vehicleVerifiedCount = vehicleRepository.count()
            vehicleUnverifiedCount = 0
        }.toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Dashboard statistics data class
     */
    data class DashboardStatistics(
        val totalUsers: Long?,
        val totalVerifiedUsers: Long?,
        val totalUnverifiedUsers: Long?,
        val totalVehicles: Long?,
        val totalParkingLots: Long?,
        val totalVerifiedParkingLots: Long?,
        val totalUnverifiedParkingLots: Long?,
        val totalActiveInvoices: Long?
    )

    /**
     * Get statistics of parking system
     */
    @GetMapping("/stats")
    fun getNumericalStatistics() = runCatching {
        // User data
        val totalUsers = userRepository.count()
        val totalVerifiedUsers = userRepository.countVerified()
        val totalUnverifiedUsers = totalUsers - totalVerifiedUsers
        // TODO: New Users Today
        // TODO: Verified Users Today
        // TODO: Total Banned Users

        // Vehicle Data
        val totalVehicles = vehicleRepository.count()

        // Parking Lot Data
        val totalParkingLots = parkingLotRepository.count()
        val totalVerifiedParkingLots = parkingLotRepository.countVerified()
        val totalUnverifiedParkingLots = totalParkingLots - totalVerifiedParkingLots
        // TODO: Verified Parking Lots Today

        // Invoice Data
        val totalActiveInvoices = invoiceRepository.countActiveInvoices()
        // TODO: Active invoices today
        // TODO: Paid invoices today
        // TODO: Ended invoices today

        DashboardStatistics(
            totalUsers,
            totalVerifiedUsers,
            totalUnverifiedUsers,
            totalVehicles,
            totalParkingLots,
            totalVerifiedParkingLots,
            totalUnverifiedParkingLots,
            totalActiveInvoices
        ).toResponse()

    }.getOrElse { it.toErrorResponse() }
}