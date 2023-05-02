package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.vehicle.VehicleRegisterRequest
import com.chatchatabc.parking.api.application.dto.vehicle.VehicleUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    /**
     * Get all vehicles by user
     */
    @GetMapping("/get-my-vehicles")
    fun getMyVehicles(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Vehicle>>> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicles = vehicleRepository.findAllByUser(principal.userId, pageable)
            ResponseEntity.ok(
                ApiResponse(
                    vehicles,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR.name,
                    true
                )
            )
        }
    }

    /**
     * Get a vehicle by id
     */
    @GetMapping("/get/{vehicleId}")
    fun getVehicleById(
        @PathVariable vehicleId: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
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

            ResponseEntity.ok(
                ApiResponse(
                    vehicle.get(),
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(
                    null,
                    HttpStatus.BAD_REQUEST.value(),
                    ResponseNames.ERROR_NOT_FOUND.name,
                    true
                )
            )
        }
    }

    /**
     * Register a vehicle
     */
    @PostMapping("/register")
    fun registerVehicle(
        @RequestBody req: VehicleRegisterRequest
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleService.registerVehicle(principal.userId, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(
                ApiResponse(
                    vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_CREATE.name, false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.ok(
                ApiResponse(
                    null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_CREATE.name, true
                )
            )
        }
    }

    /**
     * Update a vehicle
     */
    @PutMapping("/update/{vehicleId}")
    fun updateVehicle(
        @PathVariable vehicleId: String,
        @RequestBody req: VehicleUpdateRequest
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleService.updateVehicle(principal.userId, vehicleId, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }

    /**
     * Add a user to a vehicle
     */
    @PutMapping("/add-user/{vehicleId}/{userId}")
    fun addUserToVehicle(
        @PathVariable vehicleId: String,
        @PathVariable userId: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleService.addUserToVehicle(principal.userId, vehicleId, userId)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }


    /**
     * Remove a user from a vehicle
     */
    @PutMapping("/remove-user/{vehicleId}/{userId}")
    fun removeUserFromVehicle(
        @PathVariable vehicleId: String,
        @PathVariable userId: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get user from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as User
            val vehicle = vehicleService.removeUserFromVehicle(principal.userId, vehicleId, userId)
            ResponseEntity.ok(
                ApiResponse(
                    vehicle,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_UPDATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }
}