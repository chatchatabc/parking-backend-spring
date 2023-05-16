package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.RateUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Rate
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.RateRepository
import com.chatchatabc.parking.domain.service.RateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rate")
class RateController(
    private val rateRepository: RateRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val rateService: RateService
) {

    /**
     * Get Rate By ID
     */
    @GetMapping("/{rateId}")
    fun getRateById(
        @PathVariable rateId: String
    ): ResponseEntity<ApiResponse<Rate>> {
        return try {
            val rate = rateRepository.findById(rateId).get()
            ResponseEntity.ok(ApiResponse(rate, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
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
            ResponseEntity.ok(ApiResponse(parkingLot.rate, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }

    /**
     * Update Rate By ParkingLotId
     */
    @PostMapping("/update/{parkingLotUuid}")
    fun updateRateByParkingLotUuid(
        @PathVariable parkingLotUuid: String,
        @RequestBody req: RateUpdateRequest
    ): ResponseEntity<ApiResponse<Rate>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            val currentRate: Rate
            if (parkingLot.rate != null) {
                // Create rate
                currentRate = rateService.createRate(
                    parkingLot,
                    req.type,
                    req.interval,
                    req.freeHours,
                    req.payForFreeHoursWhenExceeding,
                    req.startingRate,
                    req.rate
                )
                // Set rate as rate to a parking lot
                parkingLot.rate = currentRate
                parkingLotRepository.save(parkingLot)
            } else {
                // Update rate
                currentRate = rateService.updateRate(
                    parkingLot.rate!!.id,
                    req.type,
                    req.interval,
                    req.freeHours,
                    req.payForFreeHoursWhenExceeding,
                    req.startingRate,
                    req.rate
                )
            }
            ResponseEntity.ok(
                ApiResponse(currentRate, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false)
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().build()
        }
    }
}