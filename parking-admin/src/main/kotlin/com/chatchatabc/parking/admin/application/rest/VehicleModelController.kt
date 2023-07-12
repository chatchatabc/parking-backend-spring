package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleModelMapper
import com.chatchatabc.parking.domain.model.VehicleModel
import com.chatchatabc.parking.domain.repository.VehicleModelRepository
import com.chatchatabc.parking.domain.service.VehicleModelService
import com.chatchatabc.parking.domain.specification.VehicleModelSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.domain.vehicleModel
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-model")
class VehicleModelController(
    private val vehicleModelService: VehicleModelService,
    private val vehicleModelRepository: VehicleModelRepository
) {
    private val vehicleModelMapper = Mappers.getMapper(VehicleModelMapper::class.java)

    /**
     * Get Vehicle Models
     */
    @GetMapping
    fun getVehicleModels(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = VehicleModelSpecification()
            .withParams(params)

        // Search with Keyword
        if (params.containsKey("keyword")) {
            val keyword = params["keyword"]
            spec.or(VehicleModelSpecification().withKeyword(keyword))
        }

        vehicleModelRepository.findAll(spec, pageable).toResponse()
    }

    /**
     * Get Vehicle Model by Identifier
     */
    @GetMapping("/{id}")
    fun getVehicleModel(@PathVariable id: String) = run { id.vehicleModel.toResponse() }

    /**
     * Get Vehicle Model by Brand Identifier
     */
    @GetMapping("/brand/{id}")
    fun getVehicleModelByBrandId(
        @PathVariable id: String,
        pageable: Pageable
    ) = run { vehicleModelRepository.findAllByBrandUuid(id.vehicleBrand.brandUuid, pageable).toResponse() }

    /**
     * Admin create Vehicle Model
     */
    @Operation(
        summary = "Create Vehicle Model",
        description = "Create Vehicle Model"
    )
    @PostMapping
    fun createVehicleModel(
        @RequestBody req: VehicleModelMapper.VehicleModelRequest,
        principal: Principal
    ) = runCatching {
        val createdVehicleModel = VehicleModel().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleModelMapper.mapRequestToVehicleModel(req, createdVehicleModel)
        vehicleModelService.saveVehicleModel(createdVehicleModel)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Admin update Vehicle Model
     */
    @Operation(
        summary = "Update Vehicle Model",
        description = "Update Vehicle Model"
    )
    @PutMapping("/{id}")
    fun updateVehicleModel(
        @RequestBody req: VehicleModelMapper.VehicleModelRequest,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleModel = id.vehicleModel
        vehicleModelMapper.mapRequestToVehicleModel(req, updatedVehicleModel)
        vehicleModelService.saveVehicleModel(updatedVehicleModel)
    }.getOrElse { it.toErrorResponse() }
}