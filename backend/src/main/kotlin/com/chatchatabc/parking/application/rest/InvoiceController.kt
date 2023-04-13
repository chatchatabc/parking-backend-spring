package com.chatchatabc.parking.application.rest

import com.chatchatabc.parking.application.dto.ErrorContent
import com.chatchatabc.parking.application.dto.InvoiceResponse
import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceService: InvoiceService,
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val parkingLotRepository: ParkingLotRepository
) {
    /**
     * Get Invoice by ID
     */
    @GetMapping("/get/{invoiceId}")
    fun getInvoice(
        @PathVariable invoiceId: String
    ): ResponseEntity<InvoiceResponse> {
        return try {
            val invoice = invoiceRepository.findById(invoiceId).get()
            ResponseEntity.ok(InvoiceResponse(invoice, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    InvoiceResponse(
                        null,
                        ErrorContent("Get Invoice Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }

    /**
     * Get invoices by vehicle id
     */
    @GetMapping("/get/vehicle/{vehicleId}")
    fun getInvoicesByVehicle(
        @PathVariable vehicleId: String,
        pageable: Pageable
    ): ResponseEntity<Page<Invoice>> {
        val vehicle = vehicleRepository.findById(vehicleId).get()
        return ResponseEntity.ok(invoiceRepository.findAllByVehicle(vehicle, pageable))
    }

    /**
     * Get invoices by parking lot id
     */
    @GetMapping("/get/parking-lot/{parkingLotId}")
    fun getInvoicesByParkingLot(
        @PathVariable parkingLotId: String,
        pageable: Pageable
    ): ResponseEntity<Page<Invoice>> {
        val parkingLot = parkingLotRepository.findById(parkingLotId).get()
        return ResponseEntity.ok(invoiceRepository.findAllByParkingLot(parkingLot, pageable))
    }

    /**
     * Create an invoice
     */
    @PostMapping("/create/{parkingLotId}/{vehicleId}")
    fun createInvoice(
        @PathVariable parkingLotId: String,
        @PathVariable vehicleId: String
    ): ResponseEntity<InvoiceResponse> {
        return try {
            val createdInvoice = invoiceService.createInvoice(parkingLotId, vehicleId)
            ResponseEntity.ok(InvoiceResponse(createdInvoice, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    InvoiceResponse(
                        null,
                        ErrorContent("Create Invoice Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }

    /**
     * End an invoice
     */
    @PostMapping("/end/{parkingLotId}/{invoiceId}")
    fun endInvoice(
        @PathVariable invoiceId: String,
        @PathVariable parkingLotId: String
    ): ResponseEntity<InvoiceResponse> {
        return try {
            val endedInvoice = invoiceService.endInvoice(parkingLotId, invoiceId)
            ResponseEntity.ok(InvoiceResponse(endedInvoice, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    InvoiceResponse(
                        null,
                        ErrorContent("End Invoice Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }

    /**
     * Pay an invoice
     */
    @PostMapping("/pay/{parkingLotId}/{invoiceId}")
    fun payInvoice(
        @PathVariable invoiceId: String,
        @PathVariable parkingLotId: String
    ): ResponseEntity<InvoiceResponse> {
        return try {
            val paidInvoice = invoiceService.payInvoice(parkingLotId, invoiceId)
            ResponseEntity.ok(InvoiceResponse(paidInvoice, null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    InvoiceResponse(
                        null,
                        ErrorContent("Pay Invoice Error", e.message ?: "Unknown Error")
                    )
                )
        }
    }
}