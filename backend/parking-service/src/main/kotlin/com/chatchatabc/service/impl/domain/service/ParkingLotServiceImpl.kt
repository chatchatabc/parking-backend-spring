package com.chatchatabc.service.impl.domain.service

import com.chatchatabc.service.domain.model.ParkingLot
import com.chatchatabc.service.domain.repository.InvoiceRepository
import com.chatchatabc.service.domain.repository.ParkingLotRepository
import com.chatchatabc.service.domain.repository.UserRepository
import com.chatchatabc.service.domain.service.ParkingLotService
import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import org.springframework.stereotype.Service

@Service
class ParkingLotServiceImpl(
    private val userRepository: UserRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceRepository: InvoiceRepository,
    private val natsConnection: Connection
) : ParkingLotService {
    private val objectMapper = ObjectMapper()

    /**
     * Register a parking lot
     */
    override fun register(ownerId: String, parkingLot: ParkingLot): ParkingLot {
        val owner = userRepository.findById(ownerId).get()
        parkingLot.owner = owner
        parkingLot.availableSlots = parkingLot.capacity
        return parkingLotRepository.save(parkingLot)
    }

    /**
     * Update a parking lot
     */
    override fun update(ownerId: String, parkingLotId: String, newParkingLotInfo: ParkingLot): ParkingLot {
        val owner = userRepository.findById(ownerId).get()
        val parkingLot = parkingLotRepository.findByIdAndOwner(parkingLotId, owner).get()
        println(parkingLot)

        // Apply Updates
        if (newParkingLotInfo.name != null) {
            parkingLot.name = newParkingLotInfo.name
        }
        if (newParkingLotInfo.rate != null) {
            parkingLot.rate = newParkingLotInfo.rate
        }
        if (newParkingLotInfo.capacity != null) {
            parkingLot.capacity = newParkingLotInfo.capacity
            // Get active invoices and update available slots to accommodate new capacity
            val activeInvoices = invoiceRepository.countActiveInvoices(parkingLotId)
            parkingLot.availableSlots = newParkingLotInfo.capacity - activeInvoices
        }
        if (newParkingLotInfo.latitude != null) {
            parkingLot.latitude = newParkingLotInfo.latitude
        }
        if (newParkingLotInfo.longitude != null) {
            parkingLot.longitude = newParkingLotInfo.longitude
        }

        // Nats publish event
        val jsonMessage = objectMapper.writeValueAsString(parkingLot)
        natsConnection.publish("parking-lots", jsonMessage.toByteArray(Charsets.UTF_8))

        return parkingLotRepository.save(parkingLot)
    }
}