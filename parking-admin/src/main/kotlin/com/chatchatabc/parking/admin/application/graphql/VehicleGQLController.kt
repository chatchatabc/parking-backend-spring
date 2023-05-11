package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class VehicleGQLController(
    private val vehicleRepository: VehicleRepository,
    private val memberRepository: MemberRepository
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

    /**
     * Get vehicle by id
     */
    @QueryMapping
    fun getVehicleById(
        @Argument id: String
    ): Optional<Vehicle> {
        return vehicleRepository.findById(id)
    }

    /**
     * Get vehicles by owner
     */
    @QueryMapping
    fun getVehiclesByOwner(
        @Argument ownerId: String,
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<Vehicle> {
        val user = memberRepository.findByMemberId(ownerId).get()
        val pr = PageRequest.of(page, size)
        val vehicles = vehicleRepository.findAllByOwner(user, pr)
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