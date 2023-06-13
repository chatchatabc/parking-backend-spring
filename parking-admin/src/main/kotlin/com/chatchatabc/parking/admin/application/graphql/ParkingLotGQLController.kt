package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository
import com.chatchatabc.parking.domain.specification.ParkingLotSpecification
import com.chatchatabc.parking.domain.parkingLot
import com.chatchatabc.parking.domain.parkingLotByOwner
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.web.common.toPagedResponse
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class ParkingLotGQLController(
    private val parkingLotRepository: ParkingLotRepository,
    private val parkingLotImageRepository: ParkingLotImageRepository
) {

    /**
     * Get parking lots
     */
    @QueryMapping
    fun getParkingLots(
        @Argument page: Int,
        @Argument size: Int,
        @Argument verified: Int? = null,
        @Argument keyword: String?,
        @Argument sortField: String? = null,
        @Argument sortBy: Int? = null,
    ) = run {
        val pr = PageRequest.of(page, size)
        var spec = ParkingLotSpecification.withKeyword(keyword ?: "")

        // Filter by verified
        // 0: not verified
        if (verified == 0) {
            spec = spec.and(ParkingLotSpecification.notVerified())
        }
        // 1: verified
        else if (verified == 1) {
            spec = spec.and(ParkingLotSpecification.verified())
        }

        // Sort
        if (sortField != null && sortBy != null) {
            spec = spec.and(ParkingLotSpecification.sortBy(sortField, sortBy))
        }

        parkingLotRepository.findAll(spec, pr).toPagedResponse()
    }

    /**
     * Get parking lot by identifier
     */
    @QueryMapping
    fun getParkingLot(@Argument id: String) = run { id.parkingLot }

    /**
     * Get User by Parking Lot Identifier
     */
    @QueryMapping
    fun getUserByParkingLot(@Argument id: String) = run { id.parkingLot.owner.user }

    /**
     * Get Parking Lot By User Identifier
     */
    @QueryMapping
    fun getParkingLotByUser(@Argument id: String) = run { id.user.userUuid.parkingLotByOwner }

    /**
     * Get Parking Lot Image Keys
     */
    @QueryMapping
    fun getParkingLotImageKeysByParkingLot(
        @Argument page: Int,
        @Argument size: Int,
        @Argument id: String
    ): MutableList<String> = run {
        val pr = PageRequest.of(page, size)
        val images = parkingLotImageRepository.findAllParkingLotKeysByParkingLotAndStatus(
            id.parkingLot.id,
            0,
            pr
        )
        images.content
    }
}