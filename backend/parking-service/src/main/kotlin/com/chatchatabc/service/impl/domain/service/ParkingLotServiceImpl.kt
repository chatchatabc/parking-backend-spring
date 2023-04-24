package com.chatchatabc.service.impl.domain.service

import com.chatchatabc.api.application.dto.parking_lot.ParkingLotDTO
import com.chatchatabc.api.domain.service.ParkingLotService
import com.chatchatabc.service.domain.model.ParkingLot
import com.chatchatabc.service.domain.repository.InvoiceRepository
import com.chatchatabc.service.domain.repository.ParkingLotRepository
import com.chatchatabc.service.domain.repository.UserRepository
import io.nats.client.Connection
import org.apache.dubbo.config.annotation.DubboService
import org.modelmapper.ModelMapper

@DubboService
class ParkingLotServiceImpl(
    private val userRepository: UserRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceRepository: InvoiceRepository,
    private val natsConnection: Connection
) : ParkingLotService {
    private val modelMapper = ModelMapper()

    /**
     * Register a parking lot
     */
    override fun registerParkingLot(
        ownerId: String,
        name: String,
        latitude: Double,
        longitude: Double,
        address: String?,
        description: String?,
        capacity: Int
    ): ParkingLotDTO {
        val owner = userRepository.findById(ownerId).get()
        val parkingLot = ParkingLot().apply {
            this.owner = owner
            this.name = name
            this.latitude = latitude
            this.longitude = longitude
            this.address = address
            this.description = description
            this.capacity = capacity
            this.availableSlots = capacity
        }
        // TODO: Add user to user_parking_lot table
        val savedParkingLot = parkingLotRepository.save(parkingLot)
        return modelMapper.map(savedParkingLot, ParkingLotDTO::class.java)
    }

    /**
     * Get parking lot by user id
     *
     * @param userId the user id
     * @return the array of parking lot DTO
     */
    override fun getParkingLotsByUserId(userId: String): Array<ParkingLotDTO> {
        // TODO: Uses owner id for now, need to change to user id
        val user = userRepository.findById(userId).get()
        val parkingLots = parkingLotRepository.findAllByOwner(user)
        return parkingLots.map { parkingLot ->
            modelMapper.map(parkingLot, ParkingLotDTO::class.java)
        }.toTypedArray()
    }

    /**
     * Update a parking lot
     */
    override fun updateParkingLot(
        userId: String,
        parkingLotId: String,
        name: String?,
        latitude: Double?,
        longitude: Double?,
        address: String?,
        description: String?,
        capacity: Int?
    ): ParkingLotDTO {
        // TODO: get user from allowed list
        val user = userRepository.findById(userId).get()
        val parkingLot = parkingLotRepository.findById(parkingLotId).get()

        // Apply Updates
        if (name != null) {
            parkingLot.name = name
        }
        if (capacity != null) {
            parkingLot.capacity = capacity
            // Get active invoices and update available slots to accommodate new capacity
            val activeInvoices = invoiceRepository.countActiveInvoices(parkingLotId)
            parkingLot.availableSlots = capacity - activeInvoices
        }
        if (latitude != null) {
            parkingLot.latitude = latitude
        }
        if (longitude != null) {
            parkingLot.longitude = longitude
        }
        if (address != null) {
            parkingLot.address = address
        }
        if (description != null) {
            parkingLot.description = description
        }

        // Nats publish event
//        val jsonMessage = objectMapper.writeValueAsString(parkingLot)
//        natsConnection.publish("parking-lots", jsonMessage.toByteArray(Charsets.UTF_8))
        val savedParkingLot = parkingLotRepository.save(parkingLot)
        return modelMapper.map(savedParkingLot, ParkingLotDTO::class.java)
    }
}