package com.chatchatabc.parking.impl;

import com.chatchatabc.parking.domain.model.VehicleModel;
import com.chatchatabc.parking.domain.repository.VehicleModelRepository;
import com.chatchatabc.parking.domain.service.VehicleModelService;
import org.springframework.stereotype.Service;

@Service
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;

    public VehicleModelServiceImpl(VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    /**
     * Save Vehicle Model
     *
     * @param vehicleModel Vehicle Model
     */
    @Override
    public void saveVehicleModel(VehicleModel vehicleModel) {
        vehicleModelRepository.save(vehicleModel);
    }
}
