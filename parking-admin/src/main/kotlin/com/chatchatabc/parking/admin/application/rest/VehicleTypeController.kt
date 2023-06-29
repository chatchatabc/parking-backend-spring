package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleTypeMapper
import com.chatchatabc.parking.domain.model.VehicleType
import com.chatchatabc.parking.domain.service.VehicleTypeService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleType
import com.chatchatabc.parking.web.common.application.toErrorResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-type")
class VehicleTypeController(
    private val vehicleTypeService: VehicleTypeService
) {
    private val vehicleTypeMapper = Mappers.getMapper(VehicleTypeMapper::class.java)

    /**
     * Admin create Vehicle Type
     */
    @Operation(
        summary = "Create Vehicle Type",
        description = "Create Vehicle Type"
    )
    @PostMapping
    fun createVehicleType(
        @RequestBody req: VehicleTypeMapper.VehicleTypeResponse,
        principal: Principal
    ) = runCatching {
        val createdVehicleType = VehicleType().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleTypeMapper.mapRequestToVehicleType(req, createdVehicleType)
        vehicleTypeService.saveVehicleType(createdVehicleType)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Admin update Vehicle Type
     */
    @Operation(
        summary = "Update Vehicle Type",
        description = "Update Vehicle Type"
    )
    @PutMapping("/{id}")
    fun updateVehicleType(
        @RequestBody req: VehicleTypeMapper.VehicleTypeResponse,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleType = id.vehicleType
        vehicleTypeMapper.mapRequestToVehicleType(req, updatedVehicleType)
        vehicleTypeService.saveVehicleType(updatedVehicleType)
    }.getOrElse { it.toErrorResponse() }
}