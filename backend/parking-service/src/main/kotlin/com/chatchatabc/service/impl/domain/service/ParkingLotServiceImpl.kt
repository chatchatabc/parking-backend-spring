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
        val savedParkingLot = parkingLotRepository.save(parkingLot)
        return modelMapper.map(savedParkingLot, ParkingLotDTO::class.java)
    }

    /**
     * Update a parking lot
     */
//    override fun update(ownerId: String, parkingLotId: String, newParkingLotInfo: ParkingLot): ParkingLot {
//        val owner = userRepository.findById(ownerId).get()
//        val parkingLot = parkingLotRepository.findByIdAndOwner(parkingLotId, owner).get()
//        println(parkingLot)
//
//        // Apply Updates
//        if (newParkingLotInfo.name != null) {
//            parkingLot.name = newParkingLotInfo.name
//        }
//        if (newParkingLotInfo.rate != null) {
//            parkingLot.rate = newParkingLotInfo.rate
//        }
//        if (newParkingLotInfo.capacity != null) {
//            parkingLot.capacity = newParkingLotInfo.capacity
//            // Get active invoices and update available slots to accommodate new capacity
//            val activeInvoices = invoiceRepository.countActiveInvoices(parkingLotId)
//            parkingLot.availableSlots = newParkingLotInfo.capacity - activeInvoices
//        }
//        if (newParkingLotInfo.latitude != null) {
//            parkingLot.latitude = newParkingLotInfo.latitude
//        }
//        if (newParkingLotInfo.longitude != null) {
//            parkingLot.longitude = newParkingLotInfo.longitude
//        }
//
//        // Nats publish event
//        val jsonMessage = objectMapper.writeValueAsString(parkingLot)
//        natsConnection.publish("parking-lots", jsonMessage.toByteArray(Charsets.UTF_8))
//
//        return parkingLotRepository.save(parkingLot)
//    }
}