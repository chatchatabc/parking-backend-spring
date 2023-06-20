package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.VehicleType;
import com.chatchatabc.parking.domain.repository.VehicleTypeRepository;
import com.chatchatabc.parking.domain.service.VehicleTypeService;
import org.springframework.stereotype.Service;

@Service
public class VehicleTypeServiceImpl implements VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeServiceImpl(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    /**
     * Save Vehicle Type
     *
     * @param vehicleType Vehicle Type
     */
    @Override
    public void saveVehicleType(VehicleType vehicleType) {
        vehicleTypeRepository.save(vehicleType);
    }
}
