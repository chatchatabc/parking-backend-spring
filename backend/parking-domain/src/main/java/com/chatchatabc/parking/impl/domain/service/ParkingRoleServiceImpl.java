package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.enums.ParkingRoleNames;
import com.chatchatabc.parking.domain.model.ParkingRole;
import com.chatchatabc.parking.domain.repository.ParkingRoleRepository;
import com.chatchatabc.parking.domain.service.ParkingRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ParkingRoleServiceImpl implements ParkingRoleService {
    @Autowired
    private ParkingRoleRepository parkingRoleRepository;

    private final Logger log = LoggerFactory.getLogger(ParkingRoleServiceImpl.class);

    /**
     * Init roles
     */
    @Override
    public void initRoles() {
        // Loop through ParkingRoleNames enum and create roles if they don't exist
        for (ParkingRoleNames roleName : ParkingRoleNames.values()) {
            Optional<ParkingRole> role = parkingRoleRepository.findByName(roleName.name());
            if (role.isEmpty()) {
                parkingRoleRepository.save(new ParkingRole(roleName.name()));
                log.info("Parking Role: " + role + " created");
            }
        }
    }
}
