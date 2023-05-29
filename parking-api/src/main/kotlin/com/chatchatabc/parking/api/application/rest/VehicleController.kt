package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.api.application.dto.VehicleRegisterRequest
import com.chatchatabc.parking.api.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {
    private val vehicleMapper = Mappers.getMapper(VehicleMapper::class.java)

    /**
     * Get all vehicles by user
     */
    @GetMapping("/get-my-vehicles")
    fun getMyVehicles(
        principal: Principal,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Vehicle>>> {
        return try {
            // Get user from security context
            val vehicles = vehicleRepository.findAllByUser(principal.name, pageable)
            ResponseEntity.ok(ApiResponse(vehicles, null))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Get a vehicle by id
     */
    @GetMapping("/get/{vehicleUuid}")
    fun getVehicleById(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get user from security context
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid)
            if (vehicle.isEmpty) {
                throw Exception("Vehicle not found")
            }
            // User should have access to this vehicle. If user is not inside vehicle users array
            if (vehicle.get().users.find { it.userUuid == principal.name } == null) {
                throw Exception("User does not have access to this vehicle")
            }

            ResponseEntity.ok(ApiResponse(vehicle.get(), null))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_NOT_FOUND.name, null))))
        }
    }

    /**
     * Register a vehicle
     */
    @PostMapping("/register")
    fun registerVehicle(
        @RequestBody req: VehicleRegisterRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.registerVehicle(principal.name, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(ApiResponse(vehicle, null))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_CREATE.name, null))))
        }
    }

    /**
     * Update vehicle request
     */
    data class VehicleUpdateRequest(
        val name: String?,
        val plateNumber: String?,
        val type: Int?,
    )

    /**
     * Update a vehicle
     */
    @PutMapping("/update/{vehicleUuid}")
    fun updateVehicle(
        @PathVariable vehicleUuid: String,
        @RequestBody req: VehicleUpdateRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            // TODO: check if user has access to vehicle or maybe make it so that the only is the only one that can update vehicle
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
            vehicleMapper.updateVehicleFromUpdateRequest(req, vehicle)
            vehicleService.updateVehicle(vehicle)
            ResponseEntity.ok(ApiResponse(null, null))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }

    /**
     * Add a user to a vehicle
     */
    @PutMapping("/add-user/{vehicleUuid}/{userUuid}")
    fun addUserToVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable userUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.addUserToVehicle(principal.name, vehicleUuid, userUuid)
            ResponseEntity.ok(ApiResponse(vehicle, null))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }


    /**
     * Remove a user from a vehicle
     */
    @PutMapping("/remove-user/{vehicleUuid}/{userUuid}")
    fun removeUserFromVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable userUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.removeUserFromVehicle(principal.name, vehicleUuid, userUuid)
            ResponseEntity.ok(ApiResponse(vehicle, null))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_UPDATE.name, null))))
        }
    }
}