package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.VehicleRegisterRequest
import com.chatchatabc.parking.api.application.dto.VehicleUpdateRequest
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Member
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
     * Get all vehicles by member
     */
    @GetMapping("/get-my-vehicles")
    fun getMyVehicles(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Vehicle>>> {
        return try {
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicles = vehicleRepository.findAllByMember(principal.memberId, pageable)
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
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicle = vehicleRepository.findById(vehicleId)
            if (vehicle.isEmpty) {
                throw Exception("Vehicle not found")
            }
            // Member should have access to this vehicle. If member is not inside vehicle members array
            if (vehicle.get().members.find { it.id == principal.id } == null) {
                throw Exception("Member does not have access to this vehicle")
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
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicle = vehicleService.registerVehicle(principal.memberId, req.name, req.plateNumber, req.type)
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
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicle = vehicleService.updateVehicle(principal.memberId, vehicleId, req.name, req.plateNumber, req.type)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }

    /**
     * Add a member to a vehicle
     */
    @PutMapping("/add-member/{vehicleId}/{memberId}")
    fun addMemberToVehicle(
        @PathVariable vehicleId: String,
        @PathVariable memberId: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicle = vehicleService.addMemberToVehicle(principal.memberId, vehicleId, memberId)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }


    /**
     * Remove a member from a vehicle
     */
    @PutMapping("/remove-member/{vehicleId}/{memberId}")
    fun removeMemberFromVehicle(
        @PathVariable vehicleId: String,
        @PathVariable memberId: String
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get member from security context
            val principal = SecurityContextHolder.getContext().authentication.principal as Member
            val vehicle = vehicleService.removeMemberFromVehicle(principal.memberId, vehicleId, memberId)
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