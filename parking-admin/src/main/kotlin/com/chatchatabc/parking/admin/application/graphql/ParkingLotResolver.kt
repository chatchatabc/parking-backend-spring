package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ParkingLotResolver(
    private val parkingLotRepository: ParkingLotRepository
) {
    /**
     * Get parking lots
     */
    @QueryMapping
    fun getParkingLots(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<ParkingLot> {
        val pr = PageRequest.of(page, size)
        val parkingLots = parkingLotRepository.findAll(pr)
        return PagedResponse(
            parkingLots.content,
            PageInfo(
                parkingLots.size,
                parkingLots.totalElements,
                parkingLots.isFirst,
                parkingLots.isLast,
                parkingLots.isEmpty
            )
        )
    }
}