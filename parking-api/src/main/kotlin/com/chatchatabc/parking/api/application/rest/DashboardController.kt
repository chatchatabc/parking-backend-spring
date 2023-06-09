package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository,
    private val parkingLotService: ParkingLotService,
    private val vehicleRepository: VehicleRepository
) {
    /**
     * Dashboard Statistics data class
     */
    data class DashboardStatistics(
        val availableParkingCapacity: Int,
        val leavingSoon: Long,
        val occupiedParkingCapacity: Long,
        val traffic: Long,
        val trafficPercentage: Double,
        val profit: BigDecimal,
        val profitPercentage: Double
    )

    /**
     * Get Dashboard Statistics
     */
    @GetMapping("/get")
    fun getDashboardStatistics(
        principal: Principal
    ): ResponseEntity<ApiResponse<DashboardStatistics>> {
        return try {
            // Query required data for calculation
            val owner = userRepository.findByUserUuid(principal.name).orElseThrow()
            val parkingLot = parkingLotRepository.findByOwner(owner.id).orElseThrow()

            // Get Start of Day
            val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
            // Get Yesterday
            val yesterday = LocalDateTime.now().minusDays(1)
            // Get End of Day
            val endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)

            // Leaving Soon Threshold
            val timeNow = LocalDateTime.now()
            val leavingSoonThreshold = LocalDateTime.now().plusHours(1)

            // Values
            val totalOccupancy = invoiceRepository.countActiveInvoicesByParkingLotUuid(parkingLot.parkingLotUuid)
            val leavingSoon =
                invoiceRepository.countLeavingVehicles(parkingLot.parkingLotUuid, timeNow, leavingSoonThreshold)

            // Calculate Traffic related data
            val traffic = invoiceRepository.countTrafficByDateRange(parkingLot.parkingLotUuid, startOfDay, endOfDay)
            val trafficYesterday =
                invoiceRepository.countTrafficByDateRange(parkingLot.parkingLotUuid, yesterday, startOfDay)
            val trafficPercentage =
                (traffic.toDouble() - trafficYesterday.toDouble()) / trafficYesterday.toDouble() * 100

            // Calculate Profit related data
            val profit = invoiceRepository.sumTotalByParkingLotUuidAndEndAtDateRange(
                parkingLot.parkingLotUuid,
                startOfDay,
                endOfDay
            ) ?: BigDecimal.ZERO
            val profitYesterday = invoiceRepository.sumTotalByParkingLotUuidAndEndAtDateRange(
                parkingLot.parkingLotUuid,
                yesterday,
                startOfDay
            ) ?: BigDecimal.ZERO
            val profitPercentage =
                (profit.toDouble() - profitYesterday.toDouble()) / profitYesterday.toDouble() * 100


            val dashboardStatistics = DashboardStatistics(
                // Capacity
                parkingLot.capacity,
                // Leaving soon
                leavingSoon,
                // Occupied parking capacity
                totalOccupancy - leavingSoon,

                // Traffic Calculation
                traffic,
                trafficPercentage,
                // Profit Calculation
                profit,
                profitPercentage
            )
            ResponseEntity.ok(ApiResponse(dashboardStatistics, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Capacity Increment Override
     */
    @Operation(
        summary = "Increment or Decrement Parking Lot Capacity Override",
        description = "type = increment or decrement. increment is default"
    )
    @PostMapping("/capacity/{type}/{parkingLotUuid}")
    fun capacityIncrement(
        @PathVariable parkingLotUuid: String,
        @PathVariable type: String
    ): ResponseEntity<ApiResponse<ParkingLot>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).orElseThrow()
            if (type == "decrement") {
                if (parkingLot.availableSlots > 0) {
                    parkingLot.availableSlots -= 1
                }
            } else {
                if (parkingLot.availableSlots < parkingLot.capacity) {
                    parkingLot.availableSlots += 1
                }
            }
            parkingLotService.saveParkingLot(parkingLot)
            ResponseEntity.ok(ApiResponse(parkingLot, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Search Vehicle if parked today
     */
    @GetMapping("/search/{parkingLotUuid}/{plateNumber}")
    fun searchVehicle(
        @PathVariable plateNumber: String,
        @PathVariable parkingLotUuid: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleRepository.findByPlateNumber(plateNumber).orElseThrow()
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).orElseThrow()

            val count = invoiceRepository.countVehicleInvoiceInstancesByParkingLotUuidAndDateRange(
                parkingLot.parkingLotUuid,
                vehicle.vehicleUuid,
                LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0),
                LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
            )
            if (count == 0L || count == null) {
                throw Exception("Vehicle not parked today")
            }
            ResponseEntity.ok(ApiResponse(vehicle, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        listOf(ErrorElement(ResponseNames.INVOICE_VEHICLE_NOT_PARKED_TODAY.name, null))
                    )
                )
        }
    }
}