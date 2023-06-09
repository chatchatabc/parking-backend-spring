package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.security.Principal
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/dashboard")
class DashboardController(
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository
) {
    /**
     * Dashboard Statistics data class
     */
    data class DashboardStatistics(
        val availableParkingCapacity: Int,
        val leavingSoon: Long,
        val occupiedParkingCapacity: Long,
        val traffic: Long,
        val profit: BigDecimal
    )

    // TODO: Implement get request
    // TODO: Get leaving soon
    // TODO: Get profit
    @GetMapping("/get")
    fun getDashboardStatistics(
        principal: Principal
    ): ResponseEntity<ApiResponse<DashboardStatistics>> {
        return try {
            // Get Start of Day
            val startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0)
            // Get End of Day
            val endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999)

            // Leaving Soon Threshold
            val leavingSoonThreshold = LocalDateTime.now().plusHours(1)
            val timeNow = LocalDateTime.now()

            val owner = userRepository.findByUserUuid(principal.name).orElseThrow()
            val parkingLot = parkingLotRepository.findByOwner(owner.id).orElseThrow()

            val dashboardStatistics = DashboardStatistics(
                // Capacity
                parkingLot.capacity,
                // Leaving soon
                0,
                // Occupied parking capacity
                invoiceRepository.countActiveInvoicesByParkingLotUuid(parkingLot.parkingLotUuid),

                // Traffic
                invoiceRepository.countTrafficByDateRange(parkingLot.parkingLotUuid, startOfDay, endOfDay),
                // Profit
                BigDecimal.valueOf(0)
            )
            ResponseEntity.ok(ApiResponse(dashboardStatistics, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    // TODO: Implement capacity increment or decrement manual

    // TODO: Implement vehicle parked search by license plate for only parked for the day (within timeframe)
}