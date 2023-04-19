package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.VehicleService
import org.springframework.stereotype.Service

@Service
class VehicleServiceImpl(
    private val vehicleRepository: VehicleRepository,
    private val userRepository: UserRepository
) : VehicleService {
    /**
     * Register a vehicle to a user
     */
    override fun register(userId: String, vehicle: Vehicle): Vehicle {
        val user = userRepository.findById(userId).get()
        vehicle.user = user
        return vehicleRepository.save(vehicle)
    }

    /**
     * Update a vehicle's information
     */
    override fun update(userId: String, vehicleId: String, newVehicleInfo: Vehicle): Vehicle {
        val user = userRepository.findById(userId).get()
        val vehicle = vehicleRepository.findByIdAndUser(vehicleId, user).get()

        if (newVehicleInfo.name != null) {
            vehicle.name = newVehicleInfo.name
        }
        if (newVehicleInfo.plateNumber != null) {
            vehicle.plateNumber = newVehicleInfo.plateNumber
        }
        if (newVehicleInfo.type != null) {
            vehicle.type = newVehicleInfo.type
        }

        return vehicleRepository.save(vehicle)
    }
}