package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.repository.RoleRepository;
import com.chatchatabc.parking.domain.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    /**
     * Initialize roles
     */
    @Override
    public void initRoles() {
        // Loop through RoleNames enum and create roles if they don't exist
        for (RoleNames roleName : RoleNames.values()) {
            Optional<Role> role = roleRepository.findByName(roleName.name());
            if (role.isEmpty()) {
                roleRepository.save(new Role(roleName.name()));
                log.info("Role: " + role + " created");
            }
        }
    }
}
