package com.chatchatabc.service.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.service.domain.model.ParkingLot
import com.chatchatabc.service.domain.model.User
import com.chatchatabc.service.domain.repository.ParkingLotRepository
import com.chatchatabc.service.domain.repository.UserRepository
import com.chatchatabc.service.domain.service.ParkingLotService
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
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository
) {
    private val mapper = ModelMapper()

    /**
     * Get parking lots by pageable
     */
    @GetMapping("/get/{parkingLotId}")
    fun get(
        @PathVariable parkingLotId: String
    ): ResponseEntity<com.chatchatabc.service.application.dto.ParkingLotResponse> {
        return try {
            val parkingLot = parkingLotRepository.findById(parkingLotId)
            if (parkingLot.isEmpty) {
                throw Exception("Parking Lot Not Found")
            }
            ResponseEntity.ok(com.chatchatabc.service.application.dto.ParkingLotResponse(parkingLot.get(), null))
        } catch (e: Exception) {
            ResponseEntity.ok(
                com.chatchatabc.service.application.dto.ParkingLotResponse(
                    null,
                    ErrorContent("Get Parking By ID Error", e.message ?: "Unknown Error")
                )
            )
        }
    }

    /**
     * Get Parking Lots By Owner
     */
    @GetMapping("/get-mine")
    fun getByOwner(
        pageable: Pageable
    ): ResponseEntity<Page<ParkingLot>> {
        return try {
            // Get Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val owner = userRepository.findById(principal.id).get()
            return ResponseEntity.ok(parkingLotRepository.findAllByOwner(owner, pageable))
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
        println("longitude: $longitude")
        println("latitude: $latitude")
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
        @RequestBody request: com.chatchatabc.service.application.dto.ParkingLotCreateRequest
    ): ResponseEntity<com.chatchatabc.service.application.dto.ParkingLotResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val parkingLot = mapper.map(request, ParkingLot::class.java)
            val createdParkingLot = parkingLotService.register(principal.id, parkingLot)
            return ResponseEntity.ok(
                com.chatchatabc.service.application.dto.ParkingLotResponse(
                    createdParkingLot,
                    null
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    com.chatchatabc.service.application.dto.ParkingLotResponse(
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
        @RequestBody request: com.chatchatabc.service.application.dto.ParkingLotUpdateRequest,
        @PathVariable parkingLotId: String
    ): ResponseEntity<com.chatchatabc.service.application.dto.ParkingLotResponse> {
        return try {
            // Get principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val parkingLot = mapper.map(request, ParkingLot::class.java)
            val updatedParkingLot = parkingLotService.update(principal.id, parkingLotId, parkingLot)
            return ResponseEntity.ok(
                com.chatchatabc.service.application.dto.ParkingLotResponse(
                    updatedParkingLot,
                    null
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    com.chatchatabc.service.application.dto.ParkingLotResponse(
                        null,
                        ErrorContent("Update Parking Lot Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}