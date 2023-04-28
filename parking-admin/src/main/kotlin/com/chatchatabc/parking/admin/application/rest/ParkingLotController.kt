package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ParkingLotController(
    private val parkingLotRepository: ParkingLotRepository
) {
    /**
     * Get parking lots
     */
    @QueryMapping
    fun getParkingLots(
        @Argument page: Int,
        @Argument size: Int
    ): Page<ParkingLot> {
        val pr = PageRequest.of(page, size)
        return parkingLotRepository.findAll(pr)
    }
}