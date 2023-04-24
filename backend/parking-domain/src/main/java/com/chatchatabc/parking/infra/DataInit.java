package com.chatchatabc.parking.infra;

import com.chatchatabc.parking.domain.service.ParkingRoleService;
import com.chatchatabc.parking.domain.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInit implements CommandLineRunner {
    @Autowired
    private RoleService roleService;

    @Autowired
    private ParkingRoleService parkingRoleService;

    private final Logger log = LoggerFactory.getLogger(DataInit.class);

    /**
     * Initialize data of application
     *
     * @param args the args
     */
    @Transactional
    @Override
    public void run(String... args) {
        log.info("Initializing data...");
        roleService.initRoles();
        parkingRoleService.initRoles();
        log.info("Data initialized.");
    }
}
