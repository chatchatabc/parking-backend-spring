package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.admin.application.mapper.RouteMapper
import com.chatchatabc.parking.domain.model.Route
import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteEdgeRepository
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.routeEdges
import com.chatchatabc.parking.domain.service.RouteService
import com.chatchatabc.parking.domain.specification.RouteSpecification
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.mapstruct.factory.Mappers
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeService: RouteService,
    private val routeRepository: RouteRepository,
    private val routeNodeRepository: RouteNodeRepository,
    private val routeEdgeRepository: RouteEdgeRepository
) {
    private val routeMapper = Mappers.getMapper(RouteMapper::class.java)

    /**
     * Get Routes
     */
    @Operation(
        summary = "Get Routes",
        description = "Get Routes"
    )
    @GetMapping
    fun getRoutes(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = RouteSpecification.withKeyword(params["keyword"] ?: "")
        routeRepository.findAll(spec, pageable).toResponse()
    }

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
    @Operation(
        summary = "Get Route nodes and edges by route identifier",
        description = "Get Route nodes and edges by route identifier"
    )
    @GetMapping("/nodes-and-edges/{id}/")
    fun getRouteNodesAndEdges(
        @PathVariable id: String
    ) = run {
        val edges = id.route.id.routeEdges
        val nodes = routeNodeRepository.findAllByIdIn(edges.flatMap { listOf(it.nodeFrom, it.nodeTo) }.toSet())
        RouteNodesAndEdges(nodes, edges).toResponse()
    }

    /**
     * Get Route by ID
     */
    @Operation(
        summary = "Get Route by ID",
        description = "Get Route by ID"
    )
    @GetMapping("/{id}")
    fun getRoute(@PathVariable id: String) = run { id.route.toResponse() }

    /**
     * Create Route
     */
    @Operation(
        summary = "Create Route",
        description = "Create Route"
    )
    @PostMapping
    fun createRoute(
        @RequestBody request: RouteMapper.RouteMapDTO
    ) = runCatching {
        val route = Route()
        routeMapper.mapRequestToRoute(request, route)
        routeService.saveRoute(route).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Update Route
     */
    @Operation(
        summary = "Update Route",
        description = "Update Route"
    )
    @PutMapping("/{id}")
    fun updateRoute(
        @PathVariable id: String,
        @RequestBody request: RouteMapper.RouteMapDTO
    ) = runCatching {
        val route = id.route
        routeMapper.mapRequestToRoute(request, route)
        routeService.saveRoute(route).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Route Go Request data class
     */
    data class RouteGoRequest(
        val startLatitude: Double,
        val startLongitude: Double,
        val endLatitude: Double,
        val endLongitude: Double
    )

    /**
     * Route Go Response data class
     */
    data class RouteGoResponse(
        val inputLocation: RouteNodeInput,
        val outputLocation: RouteNodeInput,
        val startRouteNode: RouteNode,
        val endRouteNode: RouteNode,
        val routeOptions: List<RouteOption>?
    )

    data class RouteOption(
        val name: String,
        val routeEdges: List<RouteEdge>,
        val duration: String,
        val distance: String,
        val cost: String
    )

    data class RouteNodeInput(
        val latitude: Double,
        val longitude: Double
    )

    /**
     * Get routes based on start location and end location
     */
    @PostMapping("/go")
    fun go(
        @RequestBody req: RouteGoRequest,
    ) = runCatching {
        // Find the closest node to start location
        val pr = PageRequest.of(0, 1)
        val startRouteNode =
            routeNodeRepository.findClosestNode(req.startLatitude, req.startLongitude, pr).content.first()

        // Find closes node to end location
        val endRouteNode = routeNodeRepository.findClosestNode(req.endLatitude, req.endLongitude, pr).content.first()

        // TODO: Optimize this
        val allRouteEdges = routeEdgeRepository.findAll()

        // TODO: Loop through the edges and find all possible rides to the end node
        val maxDepth = 1000000

        val paths = routeService.findAllPaths(allRouteEdges, startRouteNode.id, endRouteNode.id, maxDepth)
        val routeOptions = mutableListOf<RouteOption>()

        // Process each path into route options
        // println("All paths from ${startRouteNode.id} to ${endRouteNode.id}:")
        for ((counter, path) in paths.withIndex()) {
            // TODO: Optimize this
            val routeEdges = path.map { allRouteEdges.first { edge -> edge.id == it } }
            // Sum of the distance of all edges. Round to 2 decimal places
            val distance = routeEdges.sumOf { it.distance } / 1000
            // println(path.joinToString(" -> "))

            routeOptions.add(
                RouteOption(
                    name = "Option $counter",
                    routeEdges = routeEdges,
                    duration = "1 hour MANUAL", // TODO: Calculate duration
                    // Sum of the distance of all edges, convert to km
                    distance = "$distance km",
                    cost = "${routeService.calculateCost(distance)} pesos"
                )
            )
        }
        // println("Options length: ${paths.size}")

        RouteGoResponse(
            inputLocation = RouteNodeInput(req.startLatitude, req.startLongitude),
            outputLocation = RouteNodeInput(req.endLatitude, req.endLongitude),
            startRouteNode = startRouteNode,
            endRouteNode = endRouteNode,
            routeOptions
        ).toResponse()

    }.getOrElse { it.toErrorResponse() }
}