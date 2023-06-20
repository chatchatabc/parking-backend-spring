package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.VehicleBrandMapper
import com.chatchatabc.parking.domain.model.VehicleBrand
import com.chatchatabc.parking.domain.service.VehicleBrandService
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.web.common.application.toErrorResponse
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
     * Vehicle Brand Create Request Data Class
     */
    data class VehicleBrandCreateRequest(
        val name: String,
        val status: Int = 0
    )

    /**
     * Admin create Vehicle Brand
     */
    @PostMapping("/create")
    fun createVehicleBrand(
        @RequestBody req: VehicleBrandCreateRequest,
        principal: Principal
    ) = runCatching {
        val createdVehicleBrand = VehicleBrand().apply {
            this.createdBy = principal.name.user.id
        }
        vehicleBrandMapper.createVehicleBrandFromCreateRequest(req, createdVehicleBrand)
        vehicleBrandService.saveVehicleBrand(createdVehicleBrand)
    }.getOrElse { it.toErrorResponse() }

    /**
     * Vehicle Brand Update Request Data Class
     */
    data class VehicleBrandUpdateRequest(
        val name: String?,
        val status: Int?
    )

    /**
     * Admin update Vehicle Brand
     */
    @PutMapping("/update/{id}")
    fun updateVehicleBrand(
        @RequestBody req: VehicleBrandUpdateRequest,
        principal: Principal,
        @PathVariable id: String
    ) = runCatching {
        val updatedVehicleBrand = id.vehicleBrand
        vehicleBrandMapper.updateVehicleBrandFromUpdateRequest(req, updatedVehicleBrand)
        vehicleBrandService.saveVehicleBrand(updatedVehicleBrand)
    }.getOrElse { it.toErrorResponse() }
}