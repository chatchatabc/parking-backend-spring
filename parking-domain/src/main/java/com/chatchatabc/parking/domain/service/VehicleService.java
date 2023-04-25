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
    Vehicle registerVehicle(String userId, String name, String plateNumber, int type) throws Exception;

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
    Vehicle updateVehicle(String userId, String vehicleId, String name, String plateNumber, Integer type) throws Exception;

    /**
     * Add a user to a vehicle
     *
     * @param userId      the user id
     * @param vehicleId   the vehicle id
     * @param userToAddId the user to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle addUserToVehicle(String userId, String vehicleId, String userToAddId) throws Exception;

    /**
     * Remove a user from a vehicle
     *
     * @param userId         the user id
     * @param vehicleId      the vehicle id
     * @param userToRemoveId the user to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle removeUserFromVehicle(String userId, String vehicleId, String userToRemoveId) throws Exception;
}
