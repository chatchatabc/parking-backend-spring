package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleModelMapper
import com.chatchatabc.parking.domain.model.VehicleModel
import com.chatchatabc.parking.domain.service.VehicleModelService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleModel
import com.chatchatabc.parking.web.common.application.toErrorResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/vehicle-model")
class VehicleModelController(
    private val vehicleModelService: VehicleModelService
) {
    private val vehicleModelMapper = Mappers.getMapper(VehicleModelMapper::class.java)

    /**
     * Vehicle model create request
     */
    data class VehicleModelCreateRequest(
        val brandUuid: String,
        val name: String,
        val status: Int = 0
    )

    /**
     * Admin create Vehicle Model
     */
    @Operation(
        summary = "Create Vehicle Model",
        description = "Create Vehicle Model"
    )
    @PostMapping
    fun createVehicleModel(
        @RequestBody req: VehicleModelCreateRequest,
        principal: Principal
    ) = runCatching {
        val createdVehicleModel = VehicleModel().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleModelMapper.createVehicleModelFromCreateRequest(req, createdVehicleModel)
        vehicleModelService.saveVehicleModel(createdVehicleModel)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Vehicle model update request
     */
    data class VehicleModelUpdateRequest(
        val brandUuid: String?,
        val name: String?,
        val status: Int?
    )

    /**
     * Admin update Vehicle Model
     */
    @Operation(
        summary = "Update Vehicle Model",
        description = "Update Vehicle Model"
    )
    @PutMapping("/{id}")
    fun updateVehicleModel(
        @RequestBody req: VehicleModelUpdateRequest,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleModel = id.vehicleModel
        vehicleModelMapper.updateVehicleModelFromUpdateRequest(req, updatedVehicleModel)
        vehicleModelService.saveVehicleModel(updatedVehicleModel)
    }.getOrElse { it.toErrorResponse() }
}