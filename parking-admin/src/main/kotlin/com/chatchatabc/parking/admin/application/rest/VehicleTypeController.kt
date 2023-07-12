package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleTypeMapper
import com.chatchatabc.parking.domain.model.VehicleType
import com.chatchatabc.parking.domain.repository.VehicleTypeRepository
import com.chatchatabc.parking.domain.service.VehicleTypeService
import com.chatchatabc.parking.domain.specification.VehicleTypeSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleType
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-type")
class VehicleTypeController(
    private val vehicleTypeService: VehicleTypeService,
    private val vehicleTypeRepository: VehicleTypeRepository
) {
    private val vehicleTypeMapper = Mappers.getMapper(VehicleTypeMapper::class.java)

    /**
     * Get Vehicle Types
     */
    @GetMapping
    fun getVehicleTypes(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = VehicleTypeSpecification()
            .withParams(params)

        // Search with Keyword
        if (params.containsKey("keyword")) {
            val keyword = params["keyword"]
            spec.or(VehicleTypeSpecification().withKeyword(keyword))
        }

        vehicleTypeRepository.findAll(spec, pageable).toResponse()
    }

    /**
     * Get Vehicle Type by Identifier
     */
    @GetMapping("/{id}")
    fun getVehicleType(
        @PathVariable id: String
    ) = runCatching { id.vehicleType.toResponse() }.getOrElse { it.toErrorResponse() }

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
        vehicleTypeService.saveVehicleType(createdVehicleType).toResponse()
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
        vehicleTypeService.saveVehicleType(updatedVehicleType).toResponse()
    }.getOrElse { it.toErrorResponse() }
}