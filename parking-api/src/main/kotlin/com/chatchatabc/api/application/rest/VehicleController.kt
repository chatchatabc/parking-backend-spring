package com.chatchatabc.api.application.rest

import com.chatchatabc.api.application.dto.ApiResponse
import com.chatchatabc.api.application.dto.vehicle.VehicleRegisterRequest
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {
    // TODO: Get my vehicles

    /**
     * Get a vehicle by id
     */
    @GetMapping("/get/{vehicleId}")
    fun getVehicleById(
        @PathVariable vehicleId: String
    ): ResponseEntity<ApiResponse> {
        return  try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleRepository.findById(vehicleId)
            if (vehicle.isEmpty) {
                throw Exception("Vehicle not found")
            }
            // User should have access to this vehicle. If user is not inside vehicle users array
            if (vehicle.get().users.find { it.id == principal.id } == null) {
                throw Exception("User does not have access to this vehicle")
            }

            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), "Vehicle retrieved successfully", false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), e.message ?: "Unknown Error", true))
        }
    }

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
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), "Vehicle registered successfully", false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), e.message ?: "Unknown Error", true))
        }
    }

    // TODO: Add a user to a vehicle

    // TODO: Remove a user to a vehicle

    // TODO: Update a vehicle
}