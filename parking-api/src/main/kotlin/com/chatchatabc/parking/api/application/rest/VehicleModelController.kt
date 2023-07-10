package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.repository.VehicleModelRepository
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.domain.vehicleModel
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle-model")
class VehicleModelController(
    private val vehicleModelRepository: VehicleModelRepository
) {
    /**
     * Get Vehicle Models
     */
    @Operation(
        summary = "Get Vehicle Models",
        description = "Allow users to get Vehicle Models"
    )
    @GetMapping
    fun getAll(pageable: Pageable) = vehicleModelRepository.findAll(pageable).toResponse()

    /**
     * Get Vehicle Model by identifier
     */
    @Operation(
        summary = "Get Vehicle Model by identifier",
        description = "Allow users to get Vehicle Model by identifier"
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = run { id.vehicleModel.toResponse() }

    /**
     * Get Vehicle Models by Brand identifier
     */
    @Operation(
        summary = "Get Vehicle Models by Brand identifier",
        description = "Allow users to get Vehicle Models by Brand identifier"
    )
    @GetMapping("/brand/{id}")
    fun getByBrand(@PathVariable id: String, pageable: Pageable) =
        vehicleModelRepository.findAllByBrandUuid(id.vehicleBrand.brandUuid, pageable).toResponse()

    /**
     * Get Vehicle Models by Brand identifier and Type identifier
     */
    @Operation(
        summary = "Get Vehicle Models by Brand identifier and Type identifier",
        description = "Allow users to get Vehicle Models by Brand identifier and Type identifier"
    )
    @GetMapping("/type/{typeUuid}/brand/{brandUuid}")
    fun getByTypeAndBrand(
        @PathVariable typeUuid: String,
        @PathVariable brandUuid: String,
        pageable: Pageable
    ) = vehicleModelRepository.findAllByTypeUuidAndBrandUuid(typeUuid,brandUuid,  pageable).toResponse()

}