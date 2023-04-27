package com.chatchatabc.parking.api

import com.chatchatabc.parking.DomainConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@ComponentScan("com.chatchatabc.parking")
@EnableJpaRepositories(basePackages = arrayOf("com.chatchatabc.parking.domain.repository"))
@EntityScan(basePackages = arrayOf("com.chatchatabc.parking.domain.model"))
@Import(DomainConfig::class)
class ParkingManagementApplication

fun main(args: Array<String>) {
    runApplication<ParkingManagementApplication>(*args)
}
