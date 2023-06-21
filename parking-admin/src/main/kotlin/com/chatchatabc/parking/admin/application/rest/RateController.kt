package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RateMapper
import com.chatchatabc.parking.domain.model.Rate
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.service.ParkingLotService
import com.chatchatabc.parking.domain.service.RateService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/rate")
class RateController(
    private val rateService: RateService,
    private val parkingLotService: ParkingLotService
) {
    private val rateMapper = Mappers.getMapper(RateMapper::class.java)

    /**
     * Rate Update Request
     */
    data class RateUpdateRequest(
        val type: Int?,
        val interval: Int?,
        val freeHours: Int?,
        val payForFreeHoursWhenExceeding: Boolean?,
        val startingRate: BigDecimal?,
        val rate: BigDecimal?
    )

    /**
     * Update Rate by ParkingLotId
     */
    @Operation(
        summary = "Update Rate",
        description = "Update Rate"
    )
    @PostMapping("/{parkingLotUuid}")
    fun updateRateByParkingLotUuid(
        @PathVariable parkingLotUuid: String,
        @RequestBody req: RateUpdateRequest
    ) = runCatching {
        val parkingLot = parkingLotUuid.parkingLot
        if (parkingLot.rate == null) {
            // Set rate as rate to a parking lot
            val createdRate = rateService.saveRate(
                Rate().apply {
                    rateMapper.updateRateFromUpdateRateRequest(req, this)
                    this.parkingLot = parkingLot
                })
            parkingLot.rate = createdRate
            parkingLotService.saveParkingLot(parkingLot)
        } else {
            // Update rate
            rateMapper.updateRateFromUpdateRateRequest(req, parkingLot.rate!!)
            rateService.saveRate(parkingLot.rate!!)
        }
        parkingLot.toResponse()
    }.getOrElse { it.toErrorResponse() }
}