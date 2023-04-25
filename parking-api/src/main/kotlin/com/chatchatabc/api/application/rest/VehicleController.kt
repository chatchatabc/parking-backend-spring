package com.chatchatabc.api.application.rest

import com.chatchatabc.api.application.dto.ApiResponse
import com.chatchatabc.api.application.dto.vehicle.VehicleRegisterRequest
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {
    // TODO: Get my vehicles

    // TODO: Get vehicle by id

    /**
     * Register a vehicle
     */
    @PostMapping("/register")
    fun registerVehicle(
        @RequestBody req: VehicleRegisterRequest
    ): ResponseEntity<ApiResponse> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleService.registerVehicle(principal.id, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.hashCode(), "Vehicle registered successfully", false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.hashCode(), e.message ?: "Unknown Error", true))
        }
    }

    // TODO: Add a user to a vehicle

    // TODO: Remove a user to a vehicle

    // TODO: Update a vehicle
}