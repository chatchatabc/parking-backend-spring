package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.repository.VehicleTypeRepository
import com.chatchatabc.parking.domain.vehicleType
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/vehicle-type")
class VehicleTypeController(
    private val vehicleTypeRepository: VehicleTypeRepository
) {
    /**
     * Get vehicle types
     */
    @GetMapping("/")
    fun getAll(pageable: Pageable) = vehicleTypeRepository.findAll(pageable).toResponse()

    /**
     * Get vehicle type by identifier
     */
    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = run { id.vehicleType.toResponse() }
}