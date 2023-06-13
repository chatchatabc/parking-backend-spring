package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.*
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import com.chatchatabc.parking.web.common.NatsMessage
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload.InvoicePayload
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toJson
import com.chatchatabc.parking.web.common.toResponse
import io.nats.client.Connection
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val invoiceService: InvoiceService,
    private val natsConnection: Connection
) {
    /**
     * Get Invoice by ID
     */
    @GetMapping("/{invoiceUuid}")
    fun getInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching { invoiceUuid.invoice.toResponse() }.getOrElse { it.toErrorResponse() }


    /**
     * Get invoices by vehicle uuid
     */
    @GetMapping("/vehicle/{vehicleUuid}")
    fun getInvoicesByVehicle(
        @PathVariable vehicleUuid: String,
        pageable: Pageable
    ) = runCatching {
        invoiceRepository.findAllByVehicle(vehicleUuid.vehicle.vehicleUuid, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get latest active invoices by parking lot vehicle uuid
     */
    @GetMapping("/active/{vehicleUuid}")
    fun getActiveInvoice(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).orElseThrow()
        invoiceRepository.findLatestActiveInvoice(parkingLot.parkingLotUuid, vehicle.vehicleUuid)
            .toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get invoices by parking lot uuid
     */
    @GetMapping("/parking-lot/{parkingLotUuid}")
    fun getInvoicesByParkingLot(
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ) = runCatching {
        invoiceRepository.findAllByParkingLot(parkingLotUuid.parkingLot.parkingLotUuid, pageable).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Create Invoice data class
     */
    data class InvoiceCreateRequest(
        val estimatedParkingDurationInHours: Int,
    )

    /**
     * Create an invoice
     */
    @PostMapping("/create/{vehicleUuid}")
    fun createInvoice(
        @PathVariable vehicleUuid: String,
        principal: Principal,
        @RequestBody req: InvoiceCreateRequest
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        val invoice = invoiceService.createInvoice(
            parkingLot.parkingLotUuid,
            vehicleUuid,
            req.estimatedParkingDurationInHours
        )

        // NATS publish to owner and client
        val vehicle = vehicleUuid.vehicle
        val client = vehicle.owner.user

        // Message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.INVOICE_CREATED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    vehicle.vehicleUuid,
                    invoice.invoiceUuid
                )
            ).toJson().toByteArray()
        // Publish to Client
        natsConnection.publish(client.notificationUuid, natsMessage)
        // Publish to owner
        natsConnection.publish(user.notificationUuid, natsMessage)
        invoice.toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * End an invoice
     */
    @PostMapping("/end/{invoiceUuid}")
    fun endInvoice(
        @PathVariable invoiceUuid: String,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        invoiceService.endInvoice(invoiceUuid, parkingLot.parkingLotUuid)

        // NATS publish to owner and client
        val invoice = invoiceUuid.invoice
        val vehicle = invoice.vehicleUuid.vehicle
        val client = vehicle.owner.user

        // Message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.INVOICE_ENDED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    vehicle.vehicleUuid,
                    invoice.invoiceUuid
                )
            ).toJson().toByteArray()

        // Publish to Client
        natsConnection.publish(client.notificationUuid, natsMessage)
        // Publish to owner
        natsConnection.publish(user.notificationUuid, natsMessage)
        invoice.toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Pay an invoice
     */
    @PostMapping("/pay/{invoiceId}")
    fun payInvoice(
        @PathVariable invoiceId: String,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        invoiceService.payInvoice(invoiceId, parkingLot.parkingLotUuid).toResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Estimate Invoice
     */
    @GetMapping("/estimate/{invoiceUuid}")
    fun estimateInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching {
        val invoice = invoiceUuid.invoice
        val parkingLot = invoice.parkingLotUuid.parkingLot
        invoiceService.calculateInvoice(invoice, parkingLot.rate).toResponse()
    }.getOrElse { it.toErrorResponse() }
}