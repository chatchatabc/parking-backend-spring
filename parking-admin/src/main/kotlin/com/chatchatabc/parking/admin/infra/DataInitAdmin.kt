package com.chatchatabc.parking.admin.infra

import com.chatchatabc.parking.admin.domain.service.AdminService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitAdmin(
        private val adminService: AdminService
) : CommandLineRunner {
    private val log = LoggerFactory.getLogger(DataInitAdmin::class.java)

    override fun run(vararg args: String?) {
        log.info("Initializing data for Admin...")
        adminService.initAdmin()
        log.info("Admin data initialized.")
    }
}