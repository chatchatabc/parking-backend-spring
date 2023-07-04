package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.VehicleBrandRepository
import com.chatchatabc.parking.domain.specification.VehicleBrandSpecification
import com.chatchatabc.parking.domain.vehicleBrand
import com.chatchatabc.parking.web.common.application.toPagedResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class VehicleBrandGQLController(
    private val vehicleBrandRepository: VehicleBrandRepository
) {

    /**
     * Get vehicle brands
     */
    @QueryMapping
    fun getVehicleBrands(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = VehicleBrandSpecification.withKeyword(keyword ?: "")
        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(VehicleBrandSpecification.sortBy(sortField, sortBy))
        }
        vehicleBrandRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get vehicle brand by identifier
     */
    @QueryMapping
    fun getVehicleBrand(@Argument id: String) = run { id.vehicleBrand }
}