package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.ApiResponse
import com.chatchatabc.parking.admin.application.dto.dashboard.DashboardStatistics
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.http.HttpStatus
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
            val totalUsers = userRepository.count()
            // TODO: Verified Users
            // TODO: New Users Today
            // TODO: Verified Users Today
            // TODO: Banned Users
            val totalVehicles = vehicleRepository.count()
            val totalParkingLots = parkingLotRepository.count()
            // TODO: Verified Parking Lots
            // TODO: Unverified Parking Lots
            // TODO: Verified Parking Lots Today
            val totalActiveInvoices = invoiceRepository.countActiveInvoices()
            // TODO: Active invoices today
            // TODO: Paid invoices today
            // TODO: Ended invoices today

            ResponseEntity.ok().body(
                ApiResponse(
                    DashboardStatistics(
                        totalUsers,
                        totalVehicles,
                        totalParkingLots,
                        totalActiveInvoices
                    ), HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR.name, true
                    )
                )
        }
    }
}