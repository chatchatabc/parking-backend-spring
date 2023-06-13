package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.RoleRepository
import com.chatchatabc.parking.web.common.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RoleGQLController(
    private val roleRepository: RoleRepository
) {
    /**
     * Get all roles
     */
    @QueryMapping
    fun getRoles(
        @Argument page: Int,
        @Argument size: Int,
    ) = run {
        val pr = PageRequest.of(page, size)
        roleRepository.findAll(pr).toPagedResponse()
    }
}