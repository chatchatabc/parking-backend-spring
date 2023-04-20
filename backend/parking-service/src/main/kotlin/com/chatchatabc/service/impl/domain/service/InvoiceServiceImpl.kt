package com.chatchatabc.service.impl.domain.service

import com.chatchatabc.service.domain.model.Invoice
import com.chatchatabc.service.domain.repository.InvoiceRepository
import com.chatchatabc.service.domain.repository.ParkingLotRepository
import com.chatchatabc.service.domain.repository.VehicleRepository
import com.chatchatabc.service.domain.service.InvoiceService
import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import org.springframework.stereotype.Service
import java.util.*

@Service
class InvoiceServiceImpl(
    private val parkingLotRepository: ParkingLotRepository,
    private val vehicleRepository: VehicleRepository,
    private val invoiceRepository: InvoiceRepository,
    private val natsConnection: Connection
) : InvoiceService {
    private val objectMapper = ObjectMapper()

    /**
     * Create an invoice
     */
    override fun createInvoice(parkingLotId: String, vehicleId: String): Invoice {
        val parkingLot = parkingLotRepository.findById(parkingLotId).get()
        val vehicle = vehicleRepository.findById(vehicleId).get()
        // Check if vehicle has active invoice on this parking lot and return an error
        val activeInvoices = invoiceRepository.countActiveInvoicesByVehicle(parkingLotId, vehicle)
        if (activeInvoices > 0) {
            throw Exception("Vehicle has active invoice on this parking lot")
        }
        val invoice = Invoice().apply {
            this.parkingLot = parkingLot
            this.vehicle = vehicle
//            this.rate = parkingLot.rate
            this.startAt = Date()
        }
        val savedInvoice = invoiceRepository.save(invoice)
        // Reduce parking lot capacity
        parkingLot.capacity -= 1
        parkingLotRepository.save(parkingLot)
        // Nats publish event
        val jsonMessage = objectMapper.writeValueAsString(parkingLot)
        natsConnection.publish("parking-lots", jsonMessage.toByteArray(Charsets.UTF_8))
        return savedInvoice
    }

    /**
     * End an invoice
     */
    override fun endInvoice(parkingLotId: String, invoiceId: String): Invoice {
        val parkingLot = parkingLotRepository.findById(parkingLotId).get()
        val invoice = invoiceRepository.findByIdAndParkingLot(invoiceId, parkingLot).get()
        invoice.endAt = Date()
        // Calculate total cost based on rate and start/end time
        val duration = invoice.endAt!!.time - invoice.startAt!!.time
        val hours = duration / (1000 * 60 * 60)
//        invoice.total = invoice.rate * hours.toBigDecimal()
        val savedInvoice = invoiceRepository.save(invoice)
        // Update parkingLot capacity
        parkingLot.capacity += 1
        parkingLotRepository.save(parkingLot)
        // Nats publish event
        val jsonMessage = objectMapper.writeValueAsString(parkingLot)
        natsConnection.publish("parking-lots", jsonMessage.toByteArray(Charsets.UTF_8))
        return savedInvoice
    }

    /**
     * Pay an invoice
     */
    override fun payInvoice(parkingLotId: String, invoiceId: String): Invoice {
        val parkingLot = parkingLotRepository.findById(parkingLotId).get()
        val invoice = invoiceRepository.findByIdAndParkingLot(invoiceId, parkingLot).get()
        invoice.paidAt = Date()
        return invoiceRepository.save(invoice)
    }
}