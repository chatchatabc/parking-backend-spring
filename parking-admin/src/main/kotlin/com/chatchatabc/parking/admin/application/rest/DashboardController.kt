package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.DashboardStatistics
import com.chatchatabc.parking.admin.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.http.ResponseEntity
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
     * Get statistics of parking system
     */
    @GetMapping("/get-numerical-statistics")
    fun getNumericalStatistics(): ResponseEntity<ApiResponse<DashboardStatistics>> {
        return try {
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

            ResponseEntity.ok().body(
                ApiResponse(
                    DashboardStatistics(
                        totalUsers,
                        totalVerifiedUsers,
                        totalUnverifiedUsers,
                        totalVehicles,
                        totalParkingLots,
                        totalVerifiedParkingLots,
                        totalUnverifiedParkingLots,
                        totalActiveInvoices
                    ), null
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, listOf(ErrorElement(ResponseNames.ERROR.name, null))
                    )
                )
        }
    }
}