package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.VehicleBrand;
import com.chatchatabc.parking.domain.repository.VehicleBrandRepository;
import com.chatchatabc.parking.domain.service.VehicleBrandService;
import org.springframework.stereotype.Service;

@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {
    private final VehicleBrandRepository vehicleBrandRepository;

    public VehicleBrandServiceImpl(VehicleBrandRepository vehicleBrandRepository) {
        this.vehicleBrandRepository = vehicleBrandRepository;
    }

    /**
     * Save Vehicle Brand
     *
     * @param vehicleBrand Vehicle Brand
     */
    @Override
    public void saveVehicleBrand(VehicleBrand vehicleBrand) {
        vehicleBrandRepository.save(vehicleBrand);
    }
}
