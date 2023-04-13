package com.chatchatabc.parking.impl.domain.service

import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.model.RoleNames
import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.domain.service.RoleService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class RoleServiceImpl (
    private val roleRepository: RoleRepository
) : RoleService {
    private val log = LoggerFactory.getLogger(RoleServiceImpl::class.java)

    /**
     * Initialize roles
     */
    override fun initRoles() {
        // Loop through ROLE_NAMES enum and create roles if they don't exist
        RoleNames.values().forEach { roleName ->
            val role = roleRepository.findByName(roleName.toString())
            if (role.isEmpty) {
                roleRepository.save(Role(name = roleName.toString()))
                log.info("Role created: $roleName")
            }
        }
    }
}