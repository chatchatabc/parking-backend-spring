package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.RateMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Rate
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.RateRepository
import com.chatchatabc.parking.domain.service.RateService
import com.chatchatabc.parking.web.common.ApiResponse
import com.chatchatabc.parking.web.common.ErrorElement
import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/api/rate")
class RateController(
    private val rateRepository: RateRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val rateService: RateService
) {
    private val rateMapper = Mappers.getMapper(RateMapper::class.java)

    /**
     * Get Rate By ID
     */
    @GetMapping("/{rateId}")
    fun getRateById(
        @PathVariable rateId: String
    ): ResponseEntity<ApiResponse<Rate>> {
        return try {
            val rate = rateRepository.findById(rateId).get()
            ResponseEntity.ok(ApiResponse(rate, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * Get Rate By ParkingLotId
     */
    @GetMapping("/parkingLot/{parkingLotUuid}")
    fun getRateByParkingLotUUid(
        @PathVariable parkingLotUuid: String
    ): ResponseEntity<ApiResponse<Rate>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            ResponseEntity.ok(ApiResponse(parkingLot.rate, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Update Rate By ParkingLotId
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
     * Update Rate By ParkingLotId
     */
    @PostMapping("/update/{parkingLotUuid}")
    fun updateRateByParkingLotUuid(
        @PathVariable parkingLotUuid: String,
        @RequestBody req: RateUpdateRequest
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            if (parkingLot.rate == null) {
                // Set rate as rate to a parking lot
                val createdRate = rateService.saveRate(
                    Rate().apply {
                        rateMapper.updateRateFromUpdateRateRequest(req, this)
                        this.parkingLot = parkingLot
                    })
                parkingLot.rate = createdRate
                parkingLotRepository.save(parkingLot)
            } else {
                // Update rate
                rateMapper.updateRateFromUpdateRateRequest(req, parkingLot.rate!!)
                rateService.saveRate(parkingLot.rate!!)
            }
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }
}