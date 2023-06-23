package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.RouteEdgeRepository
import com.chatchatabc.parking.domain.routeEdge
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RouteEdgeGQLController(
    private val routeEdgeRepository: RouteEdgeRepository
) {
    /**
     * Get all Route Edges
     */
    @QueryMapping
    fun getRouteEdges(
        @Argument page: Int,
        @Argument size: Int,
    ) = run {
        val pr = PageRequest.of(page, size)
        routeEdgeRepository.findAll(pr).toPagedResponse()
    }

    /**
     * Get Route Edge by any identifier
     */
    @QueryMapping
    fun getRouteEdge(@Argument id: Long) = run { id.routeEdge }
}