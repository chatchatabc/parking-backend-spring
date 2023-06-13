package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleMapper
import com.chatchatabc.parking.domain.service.VehicleService
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle")
class VehicleController(
    private val vehicleService: VehicleService
) {
    private val vehicleMapper = Mappers.getMapper(VehicleMapper::class.java)

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
    @PostMapping("/register/{userUuid}")
    fun registerVehicle(
        @PathVariable userUuid: String,
        @RequestBody req: VehicleRegisterRequest
    ) = runCatching {
        vehicleService.registerVehicle(userUuid, req.name, req.plateNumber, req.type).toResponse()
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
        val vehicle = vehicleUuid.vehicle
        vehicleMapper.updateVehicleFromUpdateRequest(req, vehicle)
        vehicleService.updateVehicle(vehicle).toResponse()
    }.getOrElse { it.toErrorResponse() }
}