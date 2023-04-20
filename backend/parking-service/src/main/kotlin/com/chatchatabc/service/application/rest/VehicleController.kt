package com.chatchatabc.service.application.rest

import com.chatchatabc.api.application.dto.ErrorContent
import com.chatchatabc.service.domain.model.User
import com.chatchatabc.service.domain.model.Vehicle
import com.chatchatabc.service.domain.repository.UserRepository
import com.chatchatabc.service.domain.repository.VehicleRepository
import com.chatchatabc.service.domain.service.VehicleService
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
        @RequestBody request: com.chatchatabc.service.application.dto.VehicleRegisterRequest
    ): ResponseEntity<com.chatchatabc.service.application.dto.VehicleResponse> {
        return try {
            // Get Principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = mapper.map(request, Vehicle::class.java)
            val createdVehicle = vehicleService.register(principal.id, vehicle)
            ResponseEntity.ok().body(com.chatchatabc.service.application.dto.VehicleResponse(createdVehicle, null))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(
                    com.chatchatabc.service.application.dto.VehicleResponse(
                        null,
                        ErrorContent("Vehicle Register Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }

    /**
     * Update a vehicle's information
     */
    @PutMapping("/update/{vehicleId}")
    fun update(
        @RequestBody request: com.chatchatabc.service.application.dto.VehicleUpdateRequest,
        @PathVariable vehicleId: String
    ): ResponseEntity<com.chatchatabc.service.application.dto.VehicleResponse> {
        return try {
            // Get Principal from Security Context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = mapper.map(request, Vehicle::class.java)
            val updatedVehicle = vehicleService.update(principal.id, vehicleId, vehicle)
            ResponseEntity.ok().body(com.chatchatabc.service.application.dto.VehicleResponse(updatedVehicle, null))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(
                    com.chatchatabc.service.application.dto.VehicleResponse(
                        null,
                        ErrorContent("Vehicle Update Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}