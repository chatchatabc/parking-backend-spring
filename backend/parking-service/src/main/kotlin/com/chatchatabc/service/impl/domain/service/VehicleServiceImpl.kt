package com.chatchatabc.service.impl.domain.service

import com.chatchatabc.service.domain.model.Vehicle
import com.chatchatabc.service.domain.repository.UserRepository
import com.chatchatabc.service.domain.repository.VehicleRepository
import com.chatchatabc.service.domain.service.VehicleService
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
        val owner = userRepository.findById(userId).get()
        vehicle.owner = owner
        return vehicleRepository.save(vehicle)
    }

    /**
     * Update a vehicle's information
     */
    override fun update(userId: String, vehicleId: String, newVehicleInfo: Vehicle): Vehicle {
        val user = userRepository.findById(userId).get()
        val vehicle = vehicleRepository.findByIdAndOwner(vehicleId, user).get()

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