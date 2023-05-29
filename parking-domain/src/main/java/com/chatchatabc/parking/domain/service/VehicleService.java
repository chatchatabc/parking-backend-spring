package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Vehicle;

public interface VehicleService {
    /**
     * Register vehicle
     *
     * @param userUuid    the user uuid
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    Vehicle registerVehicle(String userUuid, String name, String plateNumber, int type) throws Exception;

    /**
     * Update vehicle
     *
     * @param updatedVehicle the updated vehicle
     */
    void updateVehicle(Vehicle updatedVehicle) throws Exception;

    /**
     * Add a user to a vehicle
     *
     * @param userUuid    the user uuid
     * @param vehicleUuid the vehicle uuid
     * @param userToAddId the user to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle addUserToVehicle(String userUuid, String vehicleUuid, String userToAddId) throws Exception;

    /**
     * Remove a user from a vehicle
     *
     * @param userUuid       the user uuid
     * @param vehicleUuid    the vehicle uuid
     * @param userToRemoveId the user to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle removeUserFromVehicle(String userUuid, String vehicleUuid, String userToRemoveId) throws Exception;
}
