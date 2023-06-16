package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class DashboardGQLController(
    private val userRepository: UserRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val vehicleRepository: VehicleRepository
) {

    /**
     * Dashboard Pie Graph DTO
     */
    data class DashboardPieGraphDTO(
        // Parking Lots
        var parkingLotVerifiedCount: Long = 0,
        var parkingLotUnverifiedCount: Long = 0,

        // Users
        var userVerifiedCount: Long = 0,
        var userUnverifiedCount: Long = 0,

        // Vehicles
        var vehicleVerifiedCount: Long = 0,
        var vehicleUnverifiedCount: Long = 0,
    )

    /**
     * Get Dashboard Pie Graph DTO
     */
    @QueryMapping
    fun getDashboardPieGraph() = run {
        DashboardPieGraphDTO().apply {
            // Parking
            parkingLotVerifiedCount = parkingLotRepository.countVerified()
            parkingLotUnverifiedCount = parkingLotRepository.count() - parkingLotVerifiedCount

            // Users
            userVerifiedCount = userRepository.countVerified()
            userUnverifiedCount = userRepository.count() - userVerifiedCount

            // Vehicles
            // TODO: implement vehicle verification system
            vehicleVerifiedCount = vehicleRepository.count()
            vehicleUnverifiedCount = 0
        }
    }
}