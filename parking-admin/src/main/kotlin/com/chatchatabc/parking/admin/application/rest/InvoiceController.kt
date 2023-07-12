package com.chatchatabc.parking.admin.application.rest

import com.chatchatabc.parking.domain.invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.specification.InvoiceSpecification
import com.chatchatabc.parking.domain.user
import com.chatchatabc.parking.domain.vehicle
import com.chatchatabc.parking.web.common.application.toResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository
) {
    /**
     * Get Invoices
     */
    @Operation(
        summary = "Get Invoices",
        description = "Get Invoices"
    )
    @GetMapping
    fun getInvoices(
        pageable: Pageable,
        @RequestParam params: Map<String, String>
    ) = run {
        val spec = InvoiceSpecification.withKeyword(params["keyword"] ?: "")
        invoiceRepository.findAll(spec, pageable).toResponse()
    }

    /**
     * Get Invoice by Identifier
     */
    @Operation(
        summary = "Get Invoice by Identifier",
        description = "Get Invoice by Identifier"
    )
    @GetMapping("/{id}")
    fun getInvoice(@PathVariable id: String) = run { id.invoice.toResponse() }

    /**
     * Get Invoices by Vehicle Identifier
     */
    @Operation(
        summary = "Get Invoice by Vehicle Identifier",
        description = "Get Invoice by Vehicle Identifier"
    )
    @GetMapping("/vehicle/{id}")
    fun getInvoicesByVehicle(
        @PathVariable id: String,
        pageable: Pageable,
    ) = run {
        invoiceRepository.findAllByVehicle(id.vehicle.vehicleUuid, pageable).toResponse()
    }

    /**
     * Get Invoices by Parking Lot Identifier
     */
    @Operation(
        summary = "Get Invoice by Parking Lot Identifier",
        description = "Get Invoice by Parking Lot Identifier"
    )
    @GetMapping("/parking-lot/{id}")
    fun getInvoicesByParkingLot(
        @PathVariable id: String,
        pageable: Pageable,
    ) = run {
        invoiceRepository.findAllByParkingLot(id, pageable).toResponse()
    }

    /**
     * Get Invoices by User Identifier
     */
    @Operation(
        summary = "Get Invoice by User Identifier",
        description = "Get Invoice by User Identifier"
    )
    @GetMapping("/user/{id}")
    fun getInvoicesByUser(
        @PathVariable id: String,
        pageable: Pageable,
    ) = run {
        val vehicles = vehicleRepository.findVehicleIdsByOwner(id.user.id)
        invoiceRepository.findAllByVehicles(vehicles, pageable).toResponse()
    }
}