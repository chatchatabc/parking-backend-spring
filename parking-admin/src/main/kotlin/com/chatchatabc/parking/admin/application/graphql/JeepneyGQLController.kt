package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.jeepney
import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.domain.route
import com.chatchatabc.parking.domain.specification.JeepneySpecification
import com.chatchatabc.parking.web.common.application.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class JeepneyGQLController(
    private val jeepneyRepository: JeepneyRepository
) {
    /**
     * Get Jeepneys
     */
    @QueryMapping
    fun getJeepneys(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = JeepneySpecification.withKeyword(keyword ?: "")

        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(JeepneySpecification.sortBy(sortField, sortBy))
        }

        jeepneyRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get Jeepney by Identifier
     */
    @QueryMapping
    fun getJeepney(@Argument id: String) = run { id.jeepney }

    /**
     * Get Jeepneys by route
     */
    @QueryMapping
    fun getJeepneysByRoute(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String,
    ) = run {
        val pr = PageRequest.of(page, size)
        jeepneyRepository.findAllByRouteUuid(id.route.routeUuid, pr).toPagedResponse()
    }
}