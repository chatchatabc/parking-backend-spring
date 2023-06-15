package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.specification.RouteSpecification
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RouteGQLController(
    private val routeRepository: RouteRepository
) {
    @QueryMapping
    fun getRoutes(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = RouteSpecification.withKeyword(keyword ?: "")
        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(RouteSpecification.sortBy(sortField, sortBy))
        }
        routeRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get Route by any identifier
     */
    @QueryMapping
    fun getRoute(@Argument id: String) = run { id.route }
}