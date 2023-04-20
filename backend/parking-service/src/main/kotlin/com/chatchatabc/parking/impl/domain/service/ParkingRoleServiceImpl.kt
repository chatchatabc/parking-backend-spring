package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.ParkingRole
import com.chatchatabc.parking.domain.model.ParkingRoleNames
import com.chatchatabc.parking.domain.repository.ParkingRoleRepository
import com.chatchatabc.parking.domain.service.ParkingRoleService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ParkingRoleServiceImpl(
    private val parkingRoleRepository: ParkingRoleRepository,
) : ParkingRoleService {
    private val log = LoggerFactory.getLogger(ParkingRoleServiceImpl::class.java)

    /**
     * Init roles
     */
    override fun initRoles() {
        // Loop through ROLE_NAMES enum and create roles if they don't exist
        ParkingRoleNames.values().forEach { roleName ->
            val role = parkingRoleRepository.findByName(roleName.toString())
            if (role.isEmpty) {
                parkingRoleRepository.save(ParkingRole(name = roleName.toString()))
                log.info("Parking Role created: $roleName")
            }
        }
    }
}