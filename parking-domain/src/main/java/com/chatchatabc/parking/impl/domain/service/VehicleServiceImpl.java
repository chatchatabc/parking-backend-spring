package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Register vehicle
     *
     * @param userId      the user id
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param type        the type of the vehicle
     * @return the vehicle
     */
    @Override
    public Vehicle registerVehicle(String userId, String name, String plateNumber, int type) throws Exception {
        Optional<User> owner = userRepository.findById(userId);
        if (owner.isEmpty()) {
            throw new Exception("User not found");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setType(type);
        vehicle.setOwner(owner.get());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        // Add user to user vehicles table
        owner.get().getVehicles().add(savedVehicle);
        userRepository.save(owner.get());
        return savedVehicle;
    }

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
    @Override
    public Vehicle updateVehicle(String userId, String vehicleId, String name, String plateNumber, Integer type) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if user has access to update vehicle
        if (!vehicle.get().getUsers().contains(user.get())) {
            throw new Exception("Vehicle not found");
        }

        // Apply changes
        if (name != null) {
            vehicle.get().setName(name);
        }
        if (plateNumber != null) {
            vehicle.get().setPlateNumber(plateNumber);
        }
        if (type != null) {
            vehicle.get().setType(type);
        }

        return vehicleRepository.save(vehicle.get());
    }

    /**
     * Add a user to a vehicle
     *
     * @param userId      the user id
     * @param vehicleId   the vehicle id
     * @param userToAddId the user to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle addUserToVehicle(String userId, String vehicleId, String userToAddId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if user has access to update vehicle
        if (!vehicle.get().getUsers().contains(user.get())) {
            throw new Exception("Vehicle not found");
        }

        Optional<User> userToAdd = userRepository.findById(userToAddId);
        if (userToAdd.isEmpty()) {
            throw new Exception("User to add not found");
        }
        // Check if user to add is already in vehicle
        if (vehicle.get().getUsers().contains(userToAdd.get())) {
            throw new Exception("User already in vehicle");
        }

        // Add vehicle to user
        userToAdd.get().getVehicles().add(vehicle.get());
        userRepository.save(userToAdd.get());

        return vehicle.get();
    }

    /**
     * Remove a user from a vehicle
     *
     * @param userId         the user id
     * @param vehicleId      the vehicle id
     * @param userToRemoveId the user to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle removeUserFromVehicle(String userId, String vehicleId, String userToRemoveId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if user has access to update vehicle
        if (!vehicle.get().getUsers().contains(user.get())) {
            throw new Exception("Vehicle not found");
        }
        // Check if user is the owner, do not remove
        if (vehicle.get().getOwner().getId().equals(userToRemoveId)) {
            throw new Exception("Cannot remove owner");
        }

        Optional<User> userToRemove = userRepository.findById(userToRemoveId);
        if (userToRemove.isEmpty()) {
            throw new Exception("User to add not found");
        }
        // Check if user to remove is not in vehicle
        if (!vehicle.get().getUsers().contains(userToRemove.get())) {
            throw new Exception("User not in vehicle");
        }

        // Remove vehicle from user
        userToRemove.get().getVehicles().remove(vehicle.get());
        userRepository.save(userToRemove.get());

        return vehicle.get();
    }
}
