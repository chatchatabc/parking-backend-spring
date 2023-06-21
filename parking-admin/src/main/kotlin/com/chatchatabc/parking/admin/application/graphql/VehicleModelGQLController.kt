package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.VehicleModelRepository
import com.chatchatabc.parking.domain.specification.VehicleModelSpecification
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.domain.vehicleModel
import com.chatchatabc.parking.web.common.application.toPagedResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class VehicleModelGQLController(
    private val vehicleModelRepository: VehicleModelRepository
) {
    /**
     * Get Vehicle Models
     */
    @QueryMapping
    fun getVehicleModels(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = VehicleModelSpecification.withKeyword(keyword ?: "")
        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(VehicleModelSpecification.sortBy(sortField, sortBy))
        }
        vehicleModelRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get Vehicle Model by identifier
     */
    @QueryMapping
    fun getVehicleModel(@Argument id: String) = run { id.vehicleModel.toResponse() }

    /**
     * Get Vehicle Model by brand identifier
     */
    @QueryMapping
    fun getVehicleModelsByBrand(
        @Argument page: Int,
        @Argument size: Int,
        @Argument brandId: String
    ) = run {
        val pr = PageRequest.of(page, size)
        vehicleModelRepository.findAllByBrandUuid(brandId.vehicleBrand.brandUuid, pr).toResponse()
    }
}