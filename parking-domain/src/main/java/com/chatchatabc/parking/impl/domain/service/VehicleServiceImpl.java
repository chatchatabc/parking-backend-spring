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
        // TODO: Check if user has roles to update vehicle
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
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
}
