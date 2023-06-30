package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(
        summary = "Get all vehicles by user",
        description = "Get all vehicles by user"
    )
    @GetMapping("/me")
    fun getMyVehicles(
        principal: Principal,
        pageable: Pageable
    ) = runCatching {
        vehicleRepository.findAllByUser(principal.name, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get a vehicle by id
     */
    @Operation(
        summary = "Get a vehicle by id",
        description = "Get a vehicle by id"
    )
    @GetMapping("/{id}")
    fun getVehicleById(
        @PathVariable id: String,
        principal: Principal
    ) = runCatching {
        // Get user from security context
        id.vehicle.toResponse()
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
        val brandUuid: String,
        val modelUuid: String,
        val typeUuid: String,
        val color: String,
        val year: String
    )

    /**
     * Register a vehicle
     */
    @Operation(
        summary = "Register a vehicle",
        description = "Register a vehicle"
    )
    @PostMapping
    fun registerVehicle(
        @RequestBody req: VehicleRegisterRequest,
        principal: Principal
    ) = runCatching {
        // TODO: Refactor to a single save service
        vehicleService.registerVehicle(
            principal.name,
            req.name,
            req.plateNumber,
            req.brandUuid,
            req.modelUuid,
            req.typeUuid,
            req.color,
            req.year
        ).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Set Vehicle Status to Pending
     */
    @Operation(
        summary = "Set Vehicle Status to Pending",
        description = "Set Vehicle Status to Pending"
    )
    @PutMapping("/set-pending/{vehicleUuid}")
    fun setVehicleStatusToPending(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ) = runCatching {
        val vehicle = vehicleUuid.vehicle
        if (vehicle.status != Vehicle.VehicleStatus.DRAFT) {
            throw Exception("Vehicle status is not draft")
        }
        vehicle.status = Vehicle.VehicleStatus.PENDING
        vehicleService.saveVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update a vehicle
     */
    @Operation(
        summary = "Update a vehicle",
        description = "Update a vehicle, will only work if vehicle status is draft"
    )
    @PutMapping("/{vehicleUuid}")
    fun updateVehicle(
        @PathVariable vehicleUuid: String,
        @RequestBody req: VehicleMapper.VehicleMapDTO,
        principal: Principal
    ) = runCatching {
        // TODO: check if user has access to vehicle or maybe make it so that the only is the only one that can update vehicle
        val vehicle = vehicleUuid.vehicle
        if (vehicle.status != Vehicle.VehicleStatus.DRAFT) {
            throw Exception("Vehicle status is not draft")
        }
        vehicleMapper.mapRequestToVehicle(req, vehicle)
        vehicleService.updateVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Add a user to a vehicle
     */
    @Operation(
        summary = "Add a user to a vehicle",
        description = "Add a user to a vehicle"
    )
    @PutMapping("/add/{vehicleUuid}/{userUuid}")
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
    @Operation(
        summary = "Remove a user from a vehicle",
        description = "Remove a user from a vehicle"
    )
    @PutMapping("/remove/{vehicleUuid}/{userUuid}")
    fun removeUserFromVehicle(
        @PathVariable vehicleUuid: String,
        @PathVariable userUuid: String,
        principal: Principal
    ) = runCatching {
        vehicleService.removeUserFromVehicle(principal.name, vehicleUuid, userUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }
}