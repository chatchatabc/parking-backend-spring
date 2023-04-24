package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Vehicle;

public interface VehicleService {
    /**
     * Register vehicle
     *
     * @param userId      the user id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    Vehicle registerVehicle(String userId, String name, String plateNumber, int type);

    /**
     * Update vehicle
     *
     * @param userId      the user id
     * @param vehicleId   the vehicle id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    Vehicle updateVehicle(String userId, String vehicleId, String name, String plateNumber, int type);
}
