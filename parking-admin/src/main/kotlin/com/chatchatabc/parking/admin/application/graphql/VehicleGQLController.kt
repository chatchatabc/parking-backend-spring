package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class VehicleGQLController(
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository
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
     * Get vehicle by uuid
     */
    @QueryMapping
    fun getVehicleByUuid(
        @Argument uuid: String
    ): Optional<Vehicle> {
        return vehicleRepository.findByVehicleUuid(uuid)
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
        val user = userRepository.findByUserUuid(ownerId).get()
        val pr = PageRequest.of(page, size)
        val vehicles = vehicleRepository.findAllByOwner(user.id, pr)
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
     * Get owner by vehicle uuid
     */
    @QueryMapping
    fun getOwnerByVehicleUuid(
        @Argument uuid: String
    ): Optional<User> {
        val vehicle = vehicleRepository.findByVehicleUuid(uuid).get()
        return userRepository.findById(vehicle.owner)
    }

    /**
     * Get Vehicle by plate number
     */
    @QueryMapping
    fun getVehicleByPlateNumber(
        @Argument plateNumber: String
    ): Optional<Vehicle> {
        return vehicleRepository.findByPlateNumber(plateNumber)
    }
}