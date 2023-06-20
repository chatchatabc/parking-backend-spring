package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleTypeMapper
import com.chatchatabc.parking.domain.model.VehicleType
import com.chatchatabc.parking.domain.service.VehicleTypeService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleType
import com.chatchatabc.parking.web.common.application.toErrorResponse
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
     * Vehicle Type Create Request Data Class
     */
    data class VehicleTypeCreateRequest(
        val name: String,
        val status: Int = 0
    )

    /**
     * Admin create Vehicle Type
     */
    @PostMapping("/create")
    fun createVehicleType(
        @RequestBody req: VehicleTypeCreateRequest,
        principal: Principal
    ) = runCatching {
        val createdVehicleType = VehicleType().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleTypeMapper.createVehicleTypeFromCreateRequest(req, createdVehicleType)
        vehicleTypeService.saveVehicleType(createdVehicleType)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Vehicle Type Update Request Data Class
     */
    data class VehicleTypeUpdateRequest(
        val name: String?,
        val status: Int?
    )

    /**
     * Admin update Vehicle Type
     */
    @PutMapping("/update/{id}")
    fun updateVehicleType(
        @RequestBody req: VehicleTypeUpdateRequest,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleType = id.vehicleType
        vehicleTypeMapper.updateVehicleTypeFromUpdateRequest(req, updatedVehicleType)
        vehicleTypeService.saveVehicleType(updatedVehicleType)
    }.getOrElse { it.toErrorResponse() }
}