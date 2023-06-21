package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.repository.VehicleBrandRepository
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle-brand")
class VehicleBrandController(
    private val vehicleBrandRepository: VehicleBrandRepository
) {
    /**
     * Get vehicle brands
     */
    @Operation(
        summary = "Get vehicle brands",
        description = "Allow users to get vehicle brands"
    )
    @GetMapping
    fun getAll(pageable: Pageable) = vehicleBrandRepository.findAll(pageable).toResponse()

    /**
     * Get vehicle brand by identifier
     */
    @Operation(
        summary = "Get vehicle brand by identifier",
        description = "Allow users to get vehicle brand by identifier"
    )
    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = run { id.vehicleBrand.toResponse() }
}