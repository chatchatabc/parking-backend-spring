package com.chatchatabc.api.domain.service;

import com.chatchatabc.api.application.dto.parking_lot.ParkingLotDTO;

public interface ParkingLotService {
    /**
     * Register a new parking lot
     *
     * @param ownerId     the owner of the parking lot
     * @param name        the name of the parking lot
     * @param latitude    the latitude of the parking lot
     * @param longitude   the longitude of the parking lot
     * @param address     the address of the parking lot
     * @param description the description of the parking lot
     * @param capacity    the capacity of the parking lot
     * @return the parking lot DTO
     */
    ParkingLotDTO registerParkingLot(
            String ownerId,
            String name,
            Double latitude,
            Double longitude,
            String address,
            String description,
            Integer capacity
    );

    /**
     * Update parking lot
     *
     * @param userId       the user id
     * @param parkingLotId the parking lot id
     * @param name         the name of the parking lot
     * @param latitude     the latitude of the parking lot
     * @param longitude    the longitude of the parking lot
     * @param address      the address of the parking lot
     * @param description  the description of the parking lot
     * @param capacity     the capacity of the parking lot
     * @return the parking lot DTO
     */
    ParkingLotDTO updateParkingLot(
            String userId,
            String parkingLotId,
            String name,
            Double latitude,
            Double longitude,
            String address,
            String description,
            Integer capacity
    );
}
