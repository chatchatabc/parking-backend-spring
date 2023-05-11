package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Role
import com.chatchatabc.parking.domain.repository.RoleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RoleController(
    private val roleRepository: RoleRepository
) {
    /**
     * Get all roles
     */
    @QueryMapping
    fun getRoles(
        @Argument page: Int,
        @Argument size: Int,
    ): PagedResponse<Role> {
        val pr = PageRequest.of(page, size)
        val roles = roleRepository.findAll(pr)
        return PagedResponse(
            roles.content,
            PageInfo(
                roles.size,
                roles.totalElements,
                roles.isFirst,
                roles.isLast,
                roles.isEmpty
            )
        )
    }
}