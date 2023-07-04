package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.VehicleTypeRepository
import com.chatchatabc.parking.domain.specification.VehicleTypeSpecification
import com.chatchatabc.parking.domain.vehicleType
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class VehicleTypeGQLController(
    private val vehicleTypeRepository: VehicleTypeRepository
) {
    /**
     * Get Vehicle Types
     */
    @QueryMapping
    fun getVehicleTypes(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = VehicleTypeSpecification.withKeyword(keyword ?: "")
        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(VehicleTypeSpecification.sortBy(sortField, sortBy))
        }
        vehicleTypeRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get Vehicle Type by identifier
     */
    @QueryMapping
    fun getVehicleType(@Argument id: String) = run { id.vehicleType }
}