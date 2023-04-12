package com.chatchatabc.parking.infra

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInit : CommandLineRunner {
    private val log = LoggerFactory.getLogger(DataInit::class.java)

    /**
     * Initialize data of application
     */
    @Transactional
    override fun run(vararg args: String?) {
        log.info("Initializing data...")
        log.info("Data initialized.")
    }
}