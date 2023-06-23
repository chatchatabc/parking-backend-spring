package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.routeNode
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class RouteNodeGQLController(
    private val routeNodeRepository: RouteNodeRepository
) {
    /**
     * Get all Route Nodes
     */
    @QueryMapping
    fun getRouteNodes(
        @Argument page: Int,
        @Argument size: Int,
    ) = run {
        val pr = PageRequest.of(page, size)
        routeNodeRepository.findAll(pr).toPagedResponse()
    }

    /**
     * Get Route Node by any identifier
     */
    @QueryMapping
    fun getRouteNode(@Argument id: Long) = run { id.routeNode }
}