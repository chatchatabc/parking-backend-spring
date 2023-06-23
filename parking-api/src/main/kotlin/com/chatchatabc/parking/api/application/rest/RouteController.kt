package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.model.RouteEdge
import com.chatchatabc.parking.domain.model.RouteNode
import com.chatchatabc.parking.domain.repository.RouteEdgeRepository
import com.chatchatabc.parking.domain.repository.RouteNodeRepository
import com.chatchatabc.parking.domain.repository.RouteRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.routeEdges
import com.chatchatabc.parking.domain.service.RouteService
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toNullResponse
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/route")
class RouteController(
    private val routeRepository: RouteRepository,
    private val routeService: RouteService,
    private val routeNodeRepository: RouteNodeRepository,
    private val routeEdgeRepository: RouteEdgeRepository
) {

    /**
     * Get All Routes
     */
    @Operation(
        summary = "Get All Routes",
        description = "Allow users to get all routes"
    )
    @GetMapping
    fun getAllRoutes(pageable: Pageable) =
        runCatching { routeRepository.findAll(pageable).toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Check last updated at of a route
     */
    @Operation(
        summary = "Check last updated at of a route",
        description = "Allow users to check last updated at of a route"
    )
    @GetMapping("/last-updated/{id}/{timestamp}")
    fun getLastUpdatedAtOfRoute(@PathVariable id: String, @PathVariable timestamp: LocalDateTime) =
        runCatching {
            val route = id.route
            if (route.updatedAt.isAfter(timestamp)) {
                route.toResponse()
            } else {
                // Return empty response
                route.toNullResponse()
            }
        }.getOrElse { it.toErrorResponse() }

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
        description = "Allow users to get route nodes and edges by route identifier"
    )
    @GetMapping("/nodes-and-edges/{id}")
    fun getRouteNodesAndEdgesById(@PathVariable id: String) =
        runCatching {
            val edges = id.route.id.routeEdges
            val nodes = routeNodeRepository.findAllByIdIn(edges.flatMap { listOf(it.nodeFrom, it.nodeTo) }.toSet())
            RouteNodesAndEdges(
                nodes,
                edges
            ).toResponse()
        }.getOrElse {
            it.toErrorResponse()
        }

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

//    fun findAllPaths(edges: List<RouteEdge>, start: Long, end: Long, maxDepth: Int): List<List<Long>> {
//        // Create graph
//        val graph = mutableMapOf<Long, MutableList<Long>>()
//        for (edge in edges) {
//            graph.computeIfAbsent(edge.nodeFrom) { mutableListOf() }.add(edge.nodeTo)
//        }
//
//        // Depth First Search
//        val visited = mutableSetOf<Long>()
//        val stack = mutableListOf(start)
//        val paths = mutableListOf<List<Long>>()
//        routeService.routeDFS(graph, visited, stack, paths, start, end, 1, maxDepth)
//
//        return paths
//    }

//    fun dfs(
//        graph: Map<Long, List<Long>>,
//        visited: MutableSet<Long>,
//        stack: MutableList<Long>,
//        paths: MutableList<List<Long>>,
//        currentNode: Long,
//        destination: Long,
//        depth: Int,
//        maxDepth: Int
//    ) {
//        if (depth > maxDepth) {
//            return // Limit the search depth
//        }
//
//        if (currentNode == destination) {
//            paths.add(stack.toList())
//        } else {
//            visited.add(currentNode)
//            for (neighbor in graph.getOrDefault(currentNode, emptyList())) {
//                if (neighbor !in visited) {
//                    stack.add(neighbor)
//                    dfs(graph, visited, stack, paths, neighbor, destination, depth + 1, maxDepth)
//                    stack.removeAt(stack.size - 1) // backtrack
//                }
//            }
//            visited.remove(currentNode) // backtrack
//        }
//    }
}