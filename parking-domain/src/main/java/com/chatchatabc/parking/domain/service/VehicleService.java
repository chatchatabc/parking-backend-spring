package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Vehicle;

public interface VehicleService {
    /**
     * Register vehicle
     *
     * @param memberId    the member id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    Vehicle registerVehicle(String memberId, String name, String plateNumber, int type) throws Exception;

    /**
     * Update vehicle
     *
     * @param updatedVehicle the updated vehicle
     */
    void updateVehicle(Vehicle updatedVehicle) throws Exception;

    /**
     * Add a member to a vehicle
     *
     * @param memberId      the member id
     * @param vehicleId     the vehicle id
     * @param memberToAddId the member to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle addMemberToVehicle(String memberId, String vehicleId, String memberToAddId) throws Exception;

    /**
     * Remove a member from a vehicle
     *
     * @param memberId         the member id
     * @param vehicleId        the vehicle id
     * @param memberToRemoveId the member to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    Vehicle removeMemberFromVehicle(String memberId, String vehicleId, String memberToRemoveId) throws Exception;
}
