package com.chatchatabc.parking.domain.service

import com.chatchatabc.parking.domain.model.ParkingLot
import org.springframework.stereotype.Service

@Service
interface ParkingLotService {

    /**
     * Register a parking lot
     */
    fun register(ownerId: String, parkingLot: ParkingLot): ParkingLot

    /**
     * Update a parking lot
     */
    fun update(ownerId: String, parkingLotId: String, newParkingLotInfo: ParkingLot): ParkingLot
}