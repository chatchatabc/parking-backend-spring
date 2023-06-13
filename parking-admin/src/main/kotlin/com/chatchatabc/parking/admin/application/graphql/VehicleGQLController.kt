package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.specification.VehicleSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class VehicleGQLController(
    private val vehicleRepository: VehicleRepository,
) {

    /**
     * Get vehicles
     */
    @QueryMapping
    fun getVehicles(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = VehicleSpecification.withKeyword(keyword ?: "")
        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(VehicleSpecification.sortBy(sortField, sortBy))
        }
        vehicleRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get vehicle by any identifier
     */
    @QueryMapping
    fun getVehicle(@Argument id: String) = run { id.vehicle }

    /**
     * Get vehicles by owner
     */
    @QueryMapping
    fun getVehiclesByOwner(
        @Argument ownerUuid: String,
        @Argument page: Int,
        @Argument size: Int
    ) = run {
        val pr = PageRequest.of(page, size)
        vehicleRepository.findAllByOwner(ownerUuid.user.id, pr).toPagedResponse()
    }

    /**
     * Get owner by vehicle identified
     */
    @QueryMapping
    fun getOwnerByVehicleId(@Argument id: String) = run { id.vehicle.owner.user }
}