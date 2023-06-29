package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleBrandMapper
import com.chatchatabc.parking.domain.model.VehicleBrand
import com.chatchatabc.parking.domain.service.VehicleBrandService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.web.common.application.toErrorResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-brand")
class VehicleBrandController(
    private val vehicleBrandService: VehicleBrandService
) {
    private val vehicleBrandMapper = Mappers.getMapper(VehicleBrandMapper::class.java)

    /**
     * Admin create Vehicle Brand
     */
    @Operation(
        summary = "Create Vehicle Brand",
        description = "Create Vehicle Brand"
    )
    @PostMapping
    fun createVehicleBrand(
        @RequestBody req: VehicleBrandMapper.VehicleBrandMapDTO,
        principal: Principal
    ) = runCatching {
        val createdVehicleBrand = VehicleBrand().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleBrandMapper.mapRequestToVehicleBrand(req, createdVehicleBrand)
        vehicleBrandService.saveVehicleBrand(createdVehicleBrand)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Admin update Vehicle Brand
     */
    @Operation(
        summary = "Update Vehicle Brand",
        description = "Update Vehicle Brand"
    )
    @PutMapping("/{id}")
    fun updateVehicleBrand(
        @RequestBody req: VehicleBrandMapper.VehicleBrandMapDTO,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleBrand = id.vehicleBrand
        vehicleBrandMapper.mapRequestToVehicleBrand(req, updatedVehicleBrand)
        vehicleBrandService.saveVehicleBrand(updatedVehicleBrand)
    }.getOrElse { it.toErrorResponse() }
}