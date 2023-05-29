package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.specification.ParkingLotSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ParkingLotGQLController(
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository
) {

    /**
     * Get parking lots
     */
    @QueryMapping
    fun getParkingLots(
        @Argument page: Int,
        @Argument size: Int,
        @Argument keyword: String?
    ): PagedResponse<ParkingLot> {
        val pr = PageRequest.of(page, size)
        val spec = ParkingLotSpecification.withKeyword(keyword ?: "")
        val parkingLots = parkingLotRepository.findAll(spec, pr)
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
     * Get parking lot by UUID
     */
    @QueryMapping
    fun getParkingLotByUuid(
        @Argument uuid: String
    ): Optional<ParkingLot> {
        return parkingLotRepository.findByParkingLotUuid(uuid)
    }

    /**
     * Get User by Parking Lot UUID
     */
    @QueryMapping
    fun getUserByParkingLotUuid(
        @Argument uuid: String
    ): Optional<User> {
        val userId = parkingLotRepository.findByParkingLotUuid(uuid).get().owner
        return userRepository.findById(userId)
    }
}