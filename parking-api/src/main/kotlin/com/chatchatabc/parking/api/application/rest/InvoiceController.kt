package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.NatsMessage
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import com.chatchatabc.parking.parkingLotByOwner
import com.chatchatabc.parking.user
import com.chatchatabc.parking.web.common.ApiResponse
import com.chatchatabc.parking.web.common.ErrorElement
import com.chatchatabc.parking.web.common.application.enums.NatsPayloadTypes
import com.chatchatabc.parking.web.common.application.nats.NatsPayload.InvoicePayload
import com.chatchatabc.parking.web.common.toErrorResponse
import com.chatchatabc.parking.web.common.toResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.nats.client.Connection
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceService: InvoiceService,
    private val userRepository: UserRepository,
    private val natsConnection: Connection
) {
    private val objectMapper = ObjectMapper()

    /**
     * Get Invoice by ID
     */
    @GetMapping("/{invoiceUuid}")
    fun getInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching {
        invoiceRepository.findByInvoiceUuid(invoiceUuid).get().toResponse()
    }.getOrElse {
        ResponseEntity.badRequest()
            .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
    }


    /**
     * Get invoices by vehicle uuid
     */
    @GetMapping("/vehicle/{vehicleUuid}")
    fun getInvoicesByVehicle(
        @PathVariable vehicleUuid: String,
        pageable: Pageable
    ) = runCatching {
        val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
        invoiceRepository.findAllByVehicle(vehicle.vehicleUuid, pageable).toResponse()
    }.getOrElse {
        it.toErrorResponse()
    }

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
    }.getOrElse {
        it.toErrorResponse()
    }

    /**
     * Get invoices by parking lot uuid
     */
    @GetMapping("/parking-lot/{parkingLotUuid}")
    fun getInvoicesByParkingLot(
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ) = runCatching {
        val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
        invoiceRepository.findAllByParkingLot(parkingLot.parkingLotUuid, pageable).toResponse()
    }.getOrElse {
        it.toErrorResponse()
    }

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
        val user = userRepository.findByUserUuid(principal.name).get()
        val parkingLot = parkingLotRepository.findByOwner(user.id).get()
        val invoice = invoiceService.createInvoice(
            parkingLot.parkingLotUuid,
            vehicleUuid,
            req.estimatedParkingDurationInHours
        )
        // NATS publish to owner and client
        val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
        val client = userRepository.findById(vehicle.owner).get()
        // Message structure
        val natsMessage = objectMapper.writeValueAsString(
            NatsMessage(
                NatsPayloadTypes.INVOICE_CREATED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    vehicle.vehicleUuid,
                    invoice.invoiceUuid
                )
            )
        ).toByteArray()
        // Publish to Client
        natsConnection.publish(client.notificationUuid, natsMessage)
        // Publish to owner
        natsConnection.publish(user.notificationUuid, natsMessage)
        invoice.toResponse()
    }.getOrElse {
        it.toErrorResponse()
    }

    /**
     * End an invoice
     */
    @PostMapping("/end/{invoiceId}")
    fun endInvoice(
        @PathVariable invoiceId: String,
        principal: Principal
    ) = runCatching {
        val user = userRepository.findByUserUuid(principal.name).get()
        val parkingLot = parkingLotRepository.findByOwner(user.id).get()
        invoiceService.endInvoice(invoiceId, parkingLot.parkingLotUuid)

        // NATS publish to owner and client
        val invoice = invoiceRepository.findByInvoiceUuid(invoiceId).get()
        val vehicle = vehicleRepository.findByVehicleUuid(invoice.vehicleUuid).get()
        val client = userRepository.findById(vehicle.owner).get()

        // Message structure
        val natsMessage = objectMapper.writeValueAsString(
            NatsMessage(
                NatsPayloadTypes.INVOICE_ENDED,
                InvoicePayload(
                    parkingLot.parkingLotUuid,
                    vehicle.vehicleUuid,
                    invoice.invoiceUuid
                )
            )
        ).toByteArray()
        // Publish to Client
        natsConnection.publish(client.notificationUuid, natsMessage)
        // Publish to owner
        natsConnection.publish(user.notificationUuid, natsMessage)
        invoice.toResponse()
    }.getOrElse {
        it.toErrorResponse()
    }

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
    }.getOrElse {
        it.toErrorResponse()
    }

    /**
     * Estimate Invoice
     */
    @GetMapping("/estimate/{invoiceUuid}")
    fun estimateInvoice(
        @PathVariable invoiceUuid: String
    ) = runCatching {
        val invoice = invoiceRepository.findByInvoiceUuid(invoiceUuid).get()
        val parkingLot = parkingLotRepository.findByParkingLotUuid(invoice.parkingLotUuid).get()
        invoiceService.calculateInvoice(invoice, parkingLot.rate).toResponse()
    }.getOrElse {
        it.toErrorResponse()
    }
}