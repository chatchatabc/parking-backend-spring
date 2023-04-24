package com.chatchatabc.parkingmanagement.infra.config.security.dto

import com.chatchatabc.api.application.dto.role.RoleDTO
import org.springframework.security.core.GrantedAuthority

class RoleDTOGrantedAuthority(
    private val roleDTO: RoleDTO
): GrantedAuthority {

    /**
     * Get role authority
     */
    override fun getAuthority(): String {
        return roleDTO.name
    }
}