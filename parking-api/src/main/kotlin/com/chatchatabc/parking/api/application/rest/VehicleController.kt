package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toResponse
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
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
    @GetMapping("/my-vehicles")
    fun getMyVehicles(
        principal: Principal,
        pageable: Pageable
    ) = runCatching {
        // TODO: Get user from security context
        vehicleRepository.findAllByUser(principal.name, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get a vehicle by id
     */
    @GetMapping("/{vehicleUuid}")
    fun getVehicleById(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ) = runCatching {
        // Get user from security context
        vehicleUuid.vehicle.toResponse()
        // TODO: User should have access to this vehicle. If user is not inside vehicle users array
        // if (vehicle.get().users.find { it.userUuid == principal.name } == null) {
        //     throw Exception("User does not have access to this vehicle")
        // }
    }.getOrElse { it.toErrorResponse() }

    /**
     * Request to register a vehicle data class
     */
    data class VehicleRegisterRequest(
        val name: String,
        val plateNumber: String,
        val type: Int,
    )

    /**
     * Register a vehicle
     */
    @PostMapping("/register")
    fun registerVehicle(
        @RequestBody req: VehicleRegisterRequest,
        principal: Principal
    ) = runCatching {
        vehicleService.registerVehicle(principal.name, req.name, req.plateNumber, req.type).toResponse()
    }.getOrElse { it.toErrorResponse() }

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
    ) = runCatching {
        // TODO: check if user has access to vehicle or maybe make it so that the only is the only one that can update vehicle
        val vehicle = vehicleUuid.vehicle
        vehicleMapper.updateVehicleFromUpdateRequest(req, vehicle)
        vehicleService.updateVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Add a user to a vehicle
     */
    @PutMapping("/add-user/{vehicleUuid}/{userUuid}")
    fun addUserToVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable userUuid: String,
        principal: Principal
    ) = runCatching {
        vehicleService.addUserToVehicle(principal.name, vehicleUuid, userUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }


    /**
     * Remove a user from a vehicle
     */
    @PutMapping("/remove-user/{vehicleUuid}/{userUuid}")
    fun removeUserFromVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable userUuid: String,
        principal: Principal
    ) = runCatching {
        vehicleService.removeUserFromVehicle(principal.name, vehicleUuid, userUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }
}