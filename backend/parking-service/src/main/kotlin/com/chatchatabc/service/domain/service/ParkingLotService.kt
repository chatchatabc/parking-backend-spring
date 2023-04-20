package com.chatchatabc.service.domain.service

import com.chatchatabc.service.domain.model.ParkingLot
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