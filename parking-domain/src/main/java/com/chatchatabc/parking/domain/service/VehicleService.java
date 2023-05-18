package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Vehicle;

public interface VehicleService {
    /**
     * Register vehicle
     *
     * @param memberUuid  the member uuid
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    Vehicle registerVehicle(String memberUuid, String name, String plateNumber, int type) throws Exception;

    /**
     * Update vehicle
     *
     * @param updatedVehicle the updated vehicle
     */
    void updateVehicle(Vehicle updatedVehicle) throws Exception;

    /**
     * Add a member to a vehicle
     *
     * @param memberUuid    the member uuid
     * @param vehicleUuid   the vehicle uuid
     * @param memberToAddId the member to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle addMemberToVehicle(String memberUuid, String vehicleUuid, String memberToAddId) throws Exception;

    /**
     * Remove a member from a vehicle
     *
     * @param memberUuid       the member uuid
     * @param vehicleUuid      the vehicle uuid
     * @param memberToRemoveId the member to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle removeMemberFromVehicle(String memberUuid, String vehicleUuid, String memberToRemoveId) throws Exception;
}
