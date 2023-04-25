package com.chatchatabc.api.application.rest

import com.chatchatabc.api.application.dto.ApiResponse
import com.chatchatabc.api.application.dto.parking_lot.ParkingLotCreateRequest
import com.chatchatabc.api.application.dto.parking_lot.ParkingLotUpdateRequest
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/parking-lot")
class ParkingLotController(
    private val parkingLotService: ParkingLotService,
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository
) {

    /**
     * Get parking lots by pageable
     */
    @GetMapping("/get/{parkingLotId}")
    fun get(
        @PathVariable parkingLotId: String
    ): ResponseEntity<ApiResponse> {
        return try {
            val parkingLot = parkingLotRepository.findById(parkingLotId).get()
            ResponseEntity.ok(ApiResponse(parkingLot, HttpStatus.OK.value(), "Get Parking Lot Successful", false))
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(null, HttpStatus.BAD_REQUEST.value(), "Parking Lot Not Found", true)
            )
        }
    }

    /**
     * Get Parking Lots By User
     */
    @GetMapping("/get-managing")
    fun getByManaging(
        pageable: Pageable
    ): ResponseEntity<Page<ParkingLot>> {
        return try {
            // Get Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val user = userRepository.findById(principal.id).get()
            val parkingLots = parkingLotRepository.findAllByOwner(user, pageable)
            return ResponseEntity.ok(parkingLots)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(null)
        }
    }

    /**
     * Get parking lots by distance
     */
    @GetMapping("/get-by-location")
    fun getByLocation(
        @RequestParam("longitude") longitude: Double,
        @RequestParam("latitude") latitude: Double,
        @RequestParam("distance") distance: Double,
    ): ResponseEntity<List<ParkingLot>> {
        var inputDistance = distance
        // Place a cap on the range, distance should not exceed 0.1km
        if (distance >= 0.1) {
            inputDistance = 0.1
        }
        return ResponseEntity.ok(
            parkingLotRepository.findByDistance(longitude, latitude, inputDistance)
        )
    }

    /**
     * Register a parking lot
     */
    @PostMapping("/register")
    fun register(
        @RequestBody req: ParkingLotCreateRequest
    ): ResponseEntity<ApiResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val createdParkingLot = parkingLotService.registerParkingLot(
                principal.id,
                req.name,
                req.latitude,
                req.longitude,
                req.address,
                req.description,
                req.capacity,
            )
            return ResponseEntity.ok(
                ApiResponse(
                    createdParkingLot,
                    HttpStatus.OK.value(),
                    "Register Parking Lot Successful",
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        e.message ?: "Unknown Error",
                        true
                    )
                )
        }
    }

    /**
     * Update a parking lot
     */
    @PutMapping("/update/{parkingLotId}")
    fun update(
        @RequestBody req: ParkingLotUpdateRequest,
        @PathVariable parkingLotId: String
    ): ResponseEntity<ApiResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val updatedParkingLot = parkingLotService.updateParkingLot(
                principal.id,
                parkingLotId,
                req.name,
                req.latitude,
                req.longitude,
                req.address,
                req.description,
                req.capacity
            )
            return ResponseEntity.ok(
                ApiResponse(
                    updatedParkingLot,
                    HttpStatus.OK.value(),
                    "Update Parking Lot Successful",
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        e.message ?: "Unknown Error",
                        true
                    )
                )
        }
    }
}