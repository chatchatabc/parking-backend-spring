package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceService: InvoiceService
) {

    /**
     * Get Invoice by ID
     */
    @GetMapping("/get/{invoiceId}")
    fun getInvoice(
        @PathVariable invoiceId: String
    ): ResponseEntity<ApiResponse<Invoice>> {
        return try {
            val invoice = invoiceRepository.findById(invoiceId).get()
            ResponseEntity.ok(ApiResponse(invoice, HttpStatus.OK.value(), ResponseNames.SUCCESS.name, false))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR.name,
                        true
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
    ): ResponseEntity<ApiResponse<Invoice>> {
        return try {
            val createdInvoice = invoiceService.createInvoice(parkingLotId, vehicleId)
            ResponseEntity.ok(
                ApiResponse(
                    createdInvoice,
                    HttpStatus.OK.value(),
                    ResponseNames.SUCCESS_CREATE.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR_CREATE.name,
                        true
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
    ): ResponseEntity<ApiResponse<Invoice>> {
        return try {
            val endedInvoice = invoiceService.endInvoice(parkingLotId, invoiceId)
            ResponseEntity.ok(
                ApiResponse(
                    endedInvoice,
                    HttpStatus.OK.value(),
                    ResponseNames.INVOICE_END_SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR.name,
                        true
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
    ): ResponseEntity<ApiResponse<Invoice>> {
        return try {
            val paidInvoice = invoiceService.payInvoice(parkingLotId, invoiceId)
            ResponseEntity.ok(
                ApiResponse(
                    paidInvoice,
                    HttpStatus.OK.value(),
                    ResponseNames.INVOICE_PAY_SUCCESS.name,
                    false
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(
                    ApiResponse(
                        null,
                        HttpStatus.BAD_REQUEST.value(),
                        ResponseNames.ERROR.name,
                        true
                    )
                )
        }
    }
}