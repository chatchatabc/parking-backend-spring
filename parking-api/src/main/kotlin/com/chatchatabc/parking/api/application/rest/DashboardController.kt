package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.parkingLotByOwner
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val invoiceRepository: InvoiceRepository,
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
    @GetMapping("/")
    fun getDashboardStatistics(
        principal: Principal
    ) = runCatching {
        // Query required data for calculation
        val owner = principal.name.user
        val parkingLot = owner.id.parkingLotByOwner

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

        DashboardStatistics(
            // Capacity
            parkingLot.capacity,
            // Leaving soon
            leavingSoon,
            // Occupied parking capacity
            totalOccupancy - leavingSoon,

            // Traffic Calculation
            traffic,
            trafficPercentage.let { if (it.isNaN() or it.isInfinite()) 0.0 else it },
            // Profit Calculation
            profit,
            profitPercentage.let { if (it.isNaN() or it.isInfinite()) 0.0 else it }
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

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
    ) = runCatching {
        val parkingLot = parkingLotUuid.parkingLot
        if (type == "decrement") {
            if (parkingLot.availableSlots > 0) {
                parkingLot.availableSlots -= 1
            }
        } else {
            if (parkingLot.availableSlots < parkingLot.capacity) {
                parkingLot.availableSlots += 1
            }
        }
        parkingLotService.saveParkingLot(parkingLot).toResponse()
    }.getOrElse { it.toErrorResponse() }


    /**
     * Search Vehicle if parked today
     */
    @GetMapping("/search/{parkingLotUuid}/{plateNumber}")
    fun searchVehicle(
        @PathVariable plateNumber: String,
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ) = runCatching {
        // Get Start of Day
        val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
        // Get End of Day
        val endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)
        vehicleRepository.findAllVehiclesByParkingLotUuidAndKeywordAndDateRangeThroughInvoices(
            parkingLotUuid,
            plateNumber,
            startOfDay,
            endOfDay,
            pageable
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }
}