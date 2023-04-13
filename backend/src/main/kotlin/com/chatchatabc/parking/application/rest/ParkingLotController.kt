package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.ParkingLotCreateRequest
import com.chatchatabc.parking.application.dto.ParkingLotResponse
import com.chatchatabc.parking.application.dto.ParkingLotUpdateRequest
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotRepository: ParkingLotRepository
) {
    private val mapper = ModelMapper()

    /**
     * Get parking lots by pageable
     */
    @GetMapping("/get")
    fun get(
        pageable: Pageable
    ): ResponseEntity<Page<ParkingLot>> {
        return ResponseEntity.ok(parkingLotRepository.findAll(pageable))
    }

    /**
     * Register a parking lot
     */
    @PostMapping("/register")
    fun register(
        @RequestBody request: ParkingLotCreateRequest
    ): ResponseEntity<ParkingLotResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val parkingLot = mapper.map(request, ParkingLot::class.java)
            val createdParkingLot = parkingLotService.register(principal.id, parkingLot)
            return ResponseEntity.ok(ParkingLotResponse(createdParkingLot, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ParkingLotResponse(
                        null,
                        ErrorContent("Register Parking Lot Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }

    /**
     * Update a parking lot
     */
    @PutMapping("/update/{parkingLotId}")
    fun update(
        @RequestBody request: ParkingLotUpdateRequest,
        @PathVariable parkingLotId: String
    ): ResponseEntity<ParkingLotResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val parkingLot = mapper.map(request, ParkingLot::class.java)
            val updatedParkingLot = parkingLotService.update(principal.id, parkingLotId, parkingLot)
            return ResponseEntity.ok(ParkingLotResponse(updatedParkingLot, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ParkingLotResponse(
                        null,
                        ErrorContent("Update Parking Lot Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}