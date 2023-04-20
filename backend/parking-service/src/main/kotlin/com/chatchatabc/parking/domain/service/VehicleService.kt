package com.chatchatabc.parking.domain.service

import com.chatchatabc.parking.domain.model.Vehicle
import org.springframework.stereotype.Service

@Service
interface VehicleService {
    /**
     * Register a vehicle to a user
     */
    fun register(userId: String, vehicle: Vehicle): Vehicle

    /**
     * Update a vehicle's information
     */
    fun update(userId: String, vehicleId: String, newVehicleInfo: Vehicle): Vehicle
}