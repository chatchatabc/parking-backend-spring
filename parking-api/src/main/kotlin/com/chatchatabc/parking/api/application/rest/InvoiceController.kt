package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.*
import com.chatchatabc.parking.domain.*
import com.chatchatabc.parking.domain.model.Vehicle
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import com.chatchatabc.parking.web.common.application.*
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload.InvoicePayload
import io.nats.client.Connection
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceService: InvoiceService,
    private val natsConnection: Connection
) {
    /**
     * Get Invoice by ID
     */
    @Operation(
        summary = "Get Invoice by ID",
        description = "Allow users to get invoice by ID"
    )
    @GetMapping("/{invoiceUuid}")
    fun getInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching { invoiceUuid.invoice.toResponse() }.getOrElse { it.toErrorResponse() }


    /**
     * Get invoices by vehicle uuid
     */
    @Operation(
        summary = "Get invoices by vehicle uuid",
        description = "Allow users to get invoices by vehicle uuid"
    )
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
    @Operation(
        summary = "Get latest active invoices by parking lot vehicle uuid",
        description = "Allow users to get latest active invoices by parking lot vehicle uuid"
    )
    @GetMapping("/active/{vehicleUuid}")
    fun getActiveInvoice(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        invoiceRepository.findLatestActiveInvoice(parkingLot.parkingLotUuid, vehicleUuid.vehicle.vehicleUuid)
            .toNullableResponse()
    }.getOrElse { it.toErrorResponse() }

    /**
     * Get invoices by parking lot uuid
     */
    @Operation(
        summary = "Get invoices by parking lot uuid",
        description = "Allow users to get invoices by parking lot uuid"
    )
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
    @Operation(
        summary = "Create an invoice",
        description = "Allow users to create an invoice. A NATS message will be published to the owner and client"
    )
    @PostMapping("/{vehicleUuid}")
    fun createInvoice(
        @PathVariable vehicleUuid: String,
        principal: Principal,
        @RequestBody req: InvoiceCreateRequest
    ) = runCatching {
        // Vehicle should be verified before creating an invoice
        val vehicle = vehicleUuid.vehicle
        if (vehicle.status != Vehicle.VehicleStatus.VERIFIED) {
            throw Exception("Vehicle is not verified")
        }

        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        val invoice = invoiceService.createInvoice(
            parkingLot.parkingLotUuid,
            vehicleUuid,
            req.estimatedParkingDurationInHours
        )

        // NATS publish to owner and client
        val client = vehicle.owner.user

        // Message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.INVOICE_CREATED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    vehicle.vehicleUuid,
                    vehicle.plateNumber,
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
     * Create Invoice data class
     */
    data class InvoiceManualCreateRequest(
        val plateNumber: String,
        val estimatedParkingDurationInHours: Int,
    )

    /**
     * Create Invoice for Vehicles with no Accounts
     */
    @Operation(
        summary = "Create an invoice for vehicles with no accounts",
        description = "Allow users to create an invoice for vehicles with no accounts. A NATS message will be published to the owner and client"
    )
    @PostMapping("/manual")
    fun createInvoiceManual(
        principal: Principal,
        @RequestBody req: InvoiceManualCreateRequest
    ) = runCatching {
        val user = principal.name.user
        val parkingLot = user.id.parkingLotByOwner
        val invoice = invoiceService.createInvoiceManual(
            parkingLot.parkingLotUuid,
            req.plateNumber,
            req.estimatedParkingDurationInHours
        )

        // Message structure
        val natsMessage =
            NatsMessage(
                NatsPayloadTypes.INVOICE_CREATED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    null,
                    req.plateNumber,
                    invoice.invoiceUuid
                )
            ).toJson().toByteArray()

        // Publish to owner
        natsConnection.publish(user.notificationUuid, natsMessage)
        invoice.toResponse()
    }.getOrElse { it.toErrorResponse() }


    /**
     * End an invoice
     */
    @Operation(
        summary = "End an invoice",
        description = "Allow users to end an invoice. A NATS message will be published to the owner and client"
    )
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
                    vehicle.plateNumber,
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
    @Operation(
        summary = "Pay an invoice",
        description = "Allow users to pay an invoice."
    )
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
    @Operation(
        summary = "Estimate Invoice",
        description = "Allow users to estimate an invoice cost based on the parking lot rate"
    )
    @GetMapping("/estimate/{invoiceUuid}")
    fun estimateInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching {
        val invoice = invoiceUuid.invoice
        val parkingLot = invoice.parkingLotUuid.parkingLot
        invoiceService.calculateInvoice(invoice, parkingLot.rate).toResponse()
    }.getOrElse { it.toErrorResponse() }
}