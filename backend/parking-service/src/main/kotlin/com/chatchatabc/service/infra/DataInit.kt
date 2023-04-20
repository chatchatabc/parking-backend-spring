package com.chatchatabc.service.infra

import com.chatchatabc.service.domain.service.ParkingRoleService
import com.chatchatabc.service.domain.service.RoleService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInit(
    private val roleService: RoleService,
    private val parkingRoleService: ParkingRoleService
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(DataInit::class.java)

    /**
     * Initialize data of application
     */
    @Transactional
    override fun run(vararg args: String?) {
        log.info("Initializing data...")
        roleService.initRoles()
        parkingRoleService.initRoles()
        log.info("Data initialized.")
    }
}