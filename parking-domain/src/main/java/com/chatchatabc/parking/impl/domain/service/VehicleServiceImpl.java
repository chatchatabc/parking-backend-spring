package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.model.file.CloudFile;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.VehicleService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    public VehicleServiceImpl(VehicleRepository vehicleRepository, UserRepository userRepository, FileStorageService fileStorageService) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Register vehicle
     *
     * @param userUuid    the user uuid
     * @param name        the name of the vehicle
     * @param plateNumber the plate number of the vehicle
     * @param modelUuid   the model of the vehicle
     * @param color       the color of the vehicle
     * @param year        the year of the vehicle
     * @return the vehicle
     */
    @Override
    public Vehicle registerVehicle(String userUuid, String name, String plateNumber, String modelUuid, String color, String year) throws Exception {
        Optional<User> owner = userRepository.findByUserUuid(userUuid);
        if (owner.isEmpty()) {
            throw new Exception("User not found");
        }
        Vehicle vehicle = new Vehicle();
        vehicle.setName(name);
        vehicle.setPlateNumber(plateNumber);
        vehicle.setModelUuid(modelUuid);
        vehicle.setColor(color);
        vehicle.setYear(year);
        vehicle.setOwner(owner.get().getId());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        // Add user to user vehicles table
        owner.get().getVehicles().add(savedVehicle);
        userRepository.save(owner.get());
        return savedVehicle;
    }

    /**
     * Save Vehicle
     *
     * @param vehicle the vehicle
     */
    @Override
    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    /**
     * Update vehicle
     *
     * @param updatedVehicle the updated vehicle
     */
    @Override
    public void updateVehicle(Vehicle updatedVehicle) {
        vehicleRepository.save(updatedVehicle);
    }

    /**
     * Add a user to a vehicle
     *
     * @param userUuid    the user uuid
     * @param vehicleUuid the vehicle uuid
     * @param userToAddId the user to add id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle addUserToVehicle(String userUuid, String vehicleUuid, String userToAddId) throws Exception {
        Optional<User> user = userRepository.findByUserUuid(userUuid);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if user has access to update vehicle
        if (!vehicle.get().getUsers().contains(user.get())) {
            throw new Exception("Vehicle not found");
        }

        Optional<User> userToAdd = userRepository.findByUserUuid(userToAddId);
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
     * @param userUuid       the user uuid
     * @param vehicleUuid    the vehicle uuid
     * @param userToRemoveId the user to remove id
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle removeUserFromVehicle(String userUuid, String vehicleUuid, String userToRemoveId) throws Exception {
        Optional<User> user = userRepository.findByUserUuid(userUuid);
        if (user.isEmpty()) {
            throw new Exception("User not found");
        }

        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }
        // Check if user has access to update vehicle
        if (!vehicle.get().getUsers().contains(user.get())) {
            throw new Exception("Vehicle not found");
        }
        // Check if user is the owner, do not remove
        // TODO: Fix this since there is no relationship anymore
//        if (vehicle.get().getOwner().getId().equals(userToRemoveId)) {
//            throw new Exception("Cannot remove owner");
//        }

        Optional<User> userToRemove = userRepository.findByUserUuid(userToRemoveId);
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

    /**
     * Upload Vehicle Image
     *
     * @param user        the user
     * @param vehicle     the vehicle
     * @param type        the image type
     * @param namespace   the namespace
     * @param inputStream the input stream
     * @param fileName    the file name
     * @param filesize    the file size
     * @param mimetype    the mime type
     * @return the vehicle
     * @throws Exception the exception
     */
    @Override
    public Vehicle uploadVehicleImage(User user, Vehicle vehicle, Integer type, String namespace, InputStream inputStream, String fileName, Long filesize, String mimetype) throws Exception {
        // Upload file to cloud storage
        CloudFile cloudFile = fileStorageService.uploadFile(user.getId(), namespace, inputStream, fileName, filesize, mimetype);

        // Save record to vehicle
        if (type == Vehicle.VehicleImageType.FRONT) {
            vehicle.setImageFront(cloudFile);
        } else if (type == Vehicle.VehicleImageType.BACK) {
            vehicle.setImageBack(cloudFile);
        } else if (type == Vehicle.VehicleImageType.LEFT) {
            vehicle.setImageLeft(cloudFile);
        } else if (type == Vehicle.VehicleImageType.RIGHT) {
            vehicle.setImageRight(cloudFile);
        } else {
            throw new Exception("Invalid image type");
        }

        return vehicleRepository.save(vehicle);
    }


}
