package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/role")
class RoleController(
    private val roleRepository: RoleRepository
) {
    /**
     * Get Roles
     */
    @GetMapping
    fun getRoles(
        pageable: Pageable
    ) = run { roleRepository.findAll(pageable).toResponse() }
}