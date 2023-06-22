package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.routeNode
import com.chatchatabc.parking.domain.service.RouteEdgeService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/route-edge")
class RouteEdgeController(
    private val routeEdgeService: RouteEdgeService,
    private val routeNodeRepository: RouteNodeRepository
) {

    /**
     * Create Route Edges data class
     */
    data class RouteEdgeCreateRequest(
        val edges: List<RouteEdges>
    )

    /**
     * Route Edges data class
     */
    data class RouteEdges(
        val routeId: Long,
        val nodeFrom: Long,
        val nodeTo: Long
    )

    /**
     * Create a Route Edge
     */
    @Operation(
        summary = "Create a Route Edge",
        description = "Create a Route Edge"
    )
    @PostMapping
    fun createEdge(
        @RequestBody request: RouteEdges,
    ) = runCatching {
        routeEdgeService.saveRouteEdge(
            RouteEdge().apply {
                this.routeId = request.routeId
                this.nodeFrom = request.nodeFrom
                this.nodeTo = request.nodeTo
                this.distance = routeEdgeService.calculateDistanceBetweenNodes(
                    request.nodeFrom.routeNode,
                    request.nodeTo.routeNode
                )
            }
        )
    }.getOrElse { it.toErrorResponse() }
}