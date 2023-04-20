package com.chatchatabc.parkingmanagement.application.rest

import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoice")
class InvoiceController {
    private val modelMapper = ModelMapper()

    /**
     * Get Invoice by ID
     */
//    @GetMapping("/get/{invoiceId}")
//    fun getInvoice(
//        @PathVariable invoiceId: String
//    ): ResponseEntity<InvoiceResponse> {
//        return try {
//            val invoice = invoiceRepository.findById(invoiceId).get()
//            val invoiceDTO = modelMapper.map(invoice, InvoiceDTO::class.java)
//            ResponseEntity.ok(InvoiceResponse(invoiceDTO, null))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity.badRequest()
//                .body(
//                    InvoiceResponse(
//                        null,
//                        ErrorContent("Get Invoice Error", e.message ?: "Unknown Error")
//                    )
//                )
//        }
//    }
//
//    /**
//     * Get invoices by vehicle id
//     */
//    @GetMapping("/get/vehicle/{vehicleId}")
//    fun getInvoicesByVehicle(
//        @PathVariable vehicleId: String,
//        pageable: Pageable
//    ): ResponseEntity<Page<Invoice>> {
//        val vehicle = vehicleRepository.findById(vehicleId).get()
//        return ResponseEntity.ok(invoiceRepository.findAllByVehicle(vehicle, pageable))
//    }
//
//    /**
//     * Get invoices by parking lot id
//     */
//    @GetMapping("/get/parking-lot/{parkingLotId}")
//    fun getInvoicesByParkingLot(
//        @PathVariable parkingLotId: String,
//        pageable: Pageable
//    ): ResponseEntity<Page<Invoice>> {
//        val parkingLot = parkingLotRepository.findById(parkingLotId).get()
//        return ResponseEntity.ok(invoiceRepository.findAllByParkingLot(parkingLot, pageable))
//    }
//
//    /**
//     * Create an invoice
//     */
//    @PostMapping("/create/{parkingLotId}/{vehicleId}")
//    fun createInvoice(
//        @PathVariable parkingLotId: String,
//        @PathVariable vehicleId: String
//    ): ResponseEntity<InvoiceResponse> {
//        return try {
//            val createdInvoice = invoiceService.createInvoice(parkingLotId, vehicleId)
//            val invoiceDTO = modelMapper.map(createdInvoice, InvoiceDTO::class.java)
//            ResponseEntity.ok(InvoiceResponse(invoiceDTO, null))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity.badRequest()
//                .body(
//                    InvoiceResponse(
//                        null,
//                        ErrorContent("Create Invoice Error", e.message ?: "Unknown Error")
//                    )
//                )
//        }
//    }
//
//    /**
//     * End an invoice
//     */
//    @PostMapping("/end/{parkingLotId}/{invoiceId}")
//    fun endInvoice(
//        @PathVariable invoiceId: String,
//        @PathVariable parkingLotId: String
//    ): ResponseEntity<InvoiceResponse> {
//        return try {
//            val endedInvoice = invoiceService.endInvoice(parkingLotId, invoiceId)
//            val invoiceDTO = modelMapper.map(endedInvoice, InvoiceDTO::class.java)
//            ResponseEntity.ok(InvoiceResponse(invoiceDTO, null))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity.badRequest()
//                .body(
//                    InvoiceResponse(
//                        null,
//                        ErrorContent("End Invoice Error", e.message ?: "Unknown Error")
//                    )
//                )
//        }
//    }
//
//    /**
//     * Pay an invoice
//     */
//    @PostMapping("/pay/{parkingLotId}/{invoiceId}")
//    fun payInvoice(
//        @PathVariable invoiceId: String,
//        @PathVariable parkingLotId: String
//    ): ResponseEntity<InvoiceResponse> {
//        return try {
//            val paidInvoice = invoiceService.payInvoice(parkingLotId, invoiceId)
//            val invoiceDTO = modelMapper.map(paidInvoice, InvoiceDTO::class.java)
//            ResponseEntity.ok(InvoiceResponse(invoiceDTO, null))
//        } catch (e: Exception) {
//            e.printStackTrace()
//            ResponseEntity.badRequest()
//                .body(
//                    InvoiceResponse(
//                        null,
//                        ErrorContent("Pay Invoice Error", e.message ?: "Unknown Error")
//                    )
//                )
//        }
//    }
}