package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.VehicleRegisterRequest
import com.chatchatabc.parking.api.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
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
     * Get all vehicles by member
     */
    @GetMapping("/get-my-vehicles")
    fun getMyVehicles(
        principal: Principal,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Vehicle>>> {
        return try {
            // Get member from security context
            val vehicles = vehicleRepository.findAllByMember(principal.name, pageable)
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
    @GetMapping("/get/{vehicleUuid}")
    fun getVehicleById(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            // Get member from security context
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid)
            if (vehicle.isEmpty) {
                throw Exception("Vehicle not found")
            }
            // Member should have access to this vehicle. If member is not inside vehicle members array
            if (vehicle.get().members.find { it.memberUuid == principal.name } == null) {
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
        @RequestBody req: VehicleRegisterRequest,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.registerVehicle(principal.name, req.name, req.plateNumber, req.type)
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
            // TODO: check if member has access to vehicle or maybe make it so that the only is the only one that can update vehicle
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
            vehicleMapper.updateVehicleFromUpdateRequest(req, vehicle)
            vehicleService.updateVehicle(vehicle)
            ResponseEntity.ok(ApiResponse(null, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }

    /**
     * Add a member to a vehicle
     */
    @PutMapping("/add-member/{vehicleUuid}/{memberUuid}")
    fun addMemberToVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable memberUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.addMemberToVehicle(principal.name, vehicleUuid, memberUuid)
            ResponseEntity.ok(ApiResponse(vehicle, HttpStatus.OK.value(), ResponseNames.SUCCESS_UPDATE.name, false))
        } catch (e: Exception) {
            ResponseEntity.ok(ApiResponse(null, HttpStatus.BAD_REQUEST.value(), ResponseNames.ERROR_UPDATE.name, true))
        }
    }


    /**
     * Remove a member from a vehicle
     */
    @PutMapping("/remove-member/{vehicleUuid}/{memberUuid}")
    fun removeMemberFromVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable memberUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Vehicle>> {
        return try {
            val vehicle = vehicleService.removeMemberFromVehicle(principal.name, vehicleUuid, memberUuid)
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