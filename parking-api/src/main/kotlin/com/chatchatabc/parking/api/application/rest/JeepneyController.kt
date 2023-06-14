package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.domain.jeepney
import com.chatchatabc.parking.domain.repository.JeepneyRepository
import com.chatchatabc.parking.web.common.application.toErrorResponse
import com.chatchatabc.parking.web.common.application.toResponse
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jeepney")
class JeepneyController(
    private val jeepneyRepository: JeepneyRepository
) {

    /**
     * Get Jeepney by Identifier
     */
    @GetMapping("/{id}")
    fun getJeepney(@PathVariable id: String) =
        runCatching { id.jeepney.toResponse() }.getOrElse { it.toErrorResponse() }

    /**
     * Get all Jeepneys
     */
    @GetMapping("/all")
    fun getAllJeepneys(
        pageable: Pageable
    ) = runCatching {
        jeepneyRepository.findAll(pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }
}