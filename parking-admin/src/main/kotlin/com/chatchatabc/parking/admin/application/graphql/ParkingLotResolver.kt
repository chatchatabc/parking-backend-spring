package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ParkingLotResolver(
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository
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

    /**
     * Get parking lot by id
     */
    @QueryMapping
    fun getParkingLotById(
        @Argument id: String
    ): Optional<ParkingLot> {
        return parkingLotRepository.findById(id)
    }

    @QueryMapping
    fun getParkingLotsByOwner(
        @Argument page: Int,
        @Argument size: Int,
        @Argument ownerId: String
    ): PagedResponse<ParkingLot> {
        val pr = PageRequest.of(page, size)
        val user = userRepository.findByUserId(ownerId).get()
        val parkingLots = parkingLotRepository.findAllByOwner(user, pr)
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