package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.VehicleRegisterRequest
import com.chatchatabc.parking.application.dto.VehicleResponse
import com.chatchatabc.parking.application.dto.VehicleUpdateRequest
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleService: VehicleService,
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository
) {
    private val mapper = ModelMapper()

    /**
     * Get all vehicles of a user with pagination
     */
    @GetMapping("/get")
    fun get(
        pageable: Pageable
    ): ResponseEntity<Page<Vehicle>> {
        // Get Principal from Security Context
        val principal = SecurityContextHolder.getContext().authentication.principal as User
        val user = userRepository.findById(principal.id).get()
        val vehicles = vehicleRepository.findAllByOwner(user, pageable)
        return ResponseEntity.ok().body(vehicles)
    }

    /**
     * Register a vehicle to a user
     */
    @PostMapping("/register")
    fun register(
        @RequestBody request: VehicleRegisterRequest
    ): ResponseEntity<VehicleResponse> {
        return try {
            // Get Principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = mapper.map(request, Vehicle::class.java)
            val createdVehicle = vehicleService.register(principal.id, vehicle)
            ResponseEntity.ok().body(VehicleResponse(createdVehicle, null))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(VehicleResponse(null, ErrorContent("Vehicle Register Error", e.message ?: "Unknown Error")))
        }
    }

    /**
     * Update a vehicle's information
     */
    @PutMapping("/update/{vehicleId}")
    fun update(
        @RequestBody request: VehicleUpdateRequest,
        @PathVariable vehicleId: String
    ): ResponseEntity<VehicleResponse> {
        return try {
            // Get Principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = mapper.map(request, Vehicle::class.java)
            val updatedVehicle = vehicleService.update(principal.id, vehicleId, vehicle)
            ResponseEntity.ok().body(VehicleResponse(updatedVehicle, null))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(VehicleResponse(null, ErrorContent("Vehicle Update Error", e.message ?: "Unknown Error")))
        }
    }
}