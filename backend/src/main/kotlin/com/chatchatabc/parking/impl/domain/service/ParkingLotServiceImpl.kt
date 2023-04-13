package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.service.ParkingLotService
import org.springframework.stereotype.Service

@Service
class ParkingLotServiceImpl(
    private val userRepository: UserRepository,
    private val parkingLotRepository: ParkingLotRepository
) : ParkingLotService {
    /**
     * Register a parking lot
     */
    override fun register(ownerId: String, parkingLot: ParkingLot): ParkingLot {
        val owner = userRepository.findById(ownerId).get()
        parkingLot.owner = owner
        parkingLot.availableSlots = parkingLot.capacity
        return parkingLotRepository.save(parkingLot)
    }

    /**
     * Update a parking lot
     */
    override fun update(ownerId: String, parkingLotId: String, newParkingLotInfo: ParkingLot): ParkingLot {
        val owner = userRepository.findById(ownerId).get()
        val parkingLot = parkingLotRepository.findByIdAndOwner(parkingLotId, owner).get()
        println(parkingLot)

        // Apply Updates
        if (newParkingLotInfo.name != null) {
            parkingLot.name = newParkingLotInfo.name
        }
        if (newParkingLotInfo.rate != null) {
            parkingLot.rate = newParkingLotInfo.rate
        }
        if (newParkingLotInfo.capacity != null) {
            parkingLot.capacity = newParkingLotInfo.capacity
            // TODO: get active invoices and update available slots
        }
        if (newParkingLotInfo.location != null) {
            parkingLot.location = newParkingLotInfo.location
        }

        return parkingLotRepository.save(parkingLot)
    }
}