package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.MemberRepository
import com.chatchatabc.parking.domain.specification.ParkingLotSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ParkingLotController(
    private val parkingLotRepository: ParkingLotRepository,
    private val memberRepository: MemberRepository
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
     * Get parking lot by id
     */
    @QueryMapping
    fun getParkingLotById(
        @Argument id: String
    ): Optional<ParkingLot> {
        return parkingLotRepository.findById(id)
    }

    /**
     * Get parking lots by owner
     */
    @QueryMapping
    fun getParkingLotsByOwner(
        @Argument page: Int,
        @Argument size: Int,
        @Argument ownerId: String
    ): PagedResponse<ParkingLot> {
        val pr = PageRequest.of(page, size)
        val user = memberRepository.findByMemberId(ownerId).get()
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