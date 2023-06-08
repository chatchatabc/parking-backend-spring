package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.User
import com.chatchatabc.parking.domain.model.file.ParkingLotImage
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository
import com.chatchatabc.parking.domain.specification.ParkingLotSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class ParkingLotGQLController(
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository,
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
        @Argument keyword: String?
    ): PagedResponse<ParkingLot> {
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

    /**
     * Get Parking Lot By User Username
     */
    @QueryMapping
    fun getParkingLotByUsername(
        @Argument username: String
    ): Optional<ParkingLot> {
        val user = userRepository.findByUsername(username).get()
        return parkingLotRepository.findByOwner(user.id)
    }

    /**
     * Get Parking Lot By User Phone
     */
    @QueryMapping
    fun getParkingLotByPhone(
        @Argument phone: String
    ): Optional<ParkingLot> {
        val user = userRepository.findByPhone(phone).get()
        return parkingLotRepository.findByOwner(user.id)
    }

    /**
     * Get Parking Lot Image Keys
     */
    @QueryMapping
    fun getParkingLotImageKeysByParkingLotUuid(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): MutableList<ParkingLotImage> {
        val pr = PageRequest.of(page, size)
        val parkingLot = parkingLotRepository.findByParkingLotUuid(uuid).get()
        val images = parkingLotImageRepository.findAllByParkingLotAndStatus(
            parkingLot.id,
            0,
            pr
        )
        return images.content
    }
}