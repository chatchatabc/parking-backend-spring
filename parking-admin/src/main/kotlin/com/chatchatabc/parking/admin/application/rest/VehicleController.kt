package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class VehicleController(
    private val vehicleRepository: VehicleRepository,
    private val vehicleService: VehicleService
) {

    /**
     * Get vehicles
     */
    @QueryMapping
    fun getVehicles(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<Vehicle> {
        val pr = PageRequest.of(page, size)
        val vehicles = vehicleRepository.findAll(pr)
        return PagedResponse(
            vehicles.content,
            PageInfo(
                vehicles.size,
                vehicles.totalElements,
                vehicles.isFirst,
                vehicles.isLast,
                vehicles.isEmpty
            )
        )
    }
}