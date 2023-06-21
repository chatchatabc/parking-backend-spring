package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.routeEdges
import com.chatchatabc.parking.domain.specification.RouteSpecification
import com.chatchatabc.parking.web.common.application.toPagedResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RouteGQLController(
    private val routeRepository: RouteRepository,
    private val routeNodeRepository: RouteNodeRepository
) {
    /**
     * Get all Routes
     */
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

    /**
     * Route Nodes and Edges data class
     */
    data class RouteNodesAndEdges(
        val nodes: List<RouteNode>,
        val edges: List<RouteEdge>
    )

    /**
     * Get Route nodes and edges by route identifier
     */
    @QueryMapping
    fun getRouteNodesAndEdges(@Argument id: String) = run {
        val edges = id.route.id.routeEdges
        val nodes = routeNodeRepository.findAllByIdIn(edges.flatMap { listOf(it.nodeFrom, it.nodeTo) }.toSet())
        println("nodes: $nodes")
        println("edges: $edges")
        RouteNodesAndEdges(
            nodes,
            edges
        )
    }
}