package com.chatchatabc.parking.api.application.rest

import com.chatchatabc.parking.api.application.dto.ApiResponse
import com.chatchatabc.parking.api.application.dto.ErrorElement
import com.chatchatabc.parking.domain.enums.ResponseNames
import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import com.chatchatabc.parking.domain.service.InvoiceService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.Principal
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/api/invoice")
class InvoiceController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val invoiceService: InvoiceService,
    private val userRepository: UserRepository
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
            ResponseEntity.ok(ApiResponse(invoice, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Get invoices by vehicle uuid
     */
    @GetMapping("/get/vehicle/{vehicleUuid}")
    fun getInvoicesByVehicle(
        @PathVariable vehicleUuid: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Invoice>>> {
        return try {
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
            return ResponseEntity.ok(ApiResponse(invoiceRepository.findAllByVehicle(vehicle.id, pageable), null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Get latest active invoices by parking lot vehicle uuid
     */
    @GetMapping("/get/active/{vehicleUuid}")
    fun getActiveInvoice(
        @PathVariable vehicleUuid: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Invoice>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            val vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).get()
            val invoice = invoiceRepository.findLatestActiveInvoice(parkingLot.id, vehicle.id)
            return ResponseEntity.ok(ApiResponse(invoice.getOrNull(), null))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Get invoices by parking lot uuid
     */
    @GetMapping("/get/parking-lot/{parkingLotUuid}")
    fun getInvoicesByParkingLot(
        @PathVariable parkingLotUuid: String,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<Invoice>>> {
        return try {
            val parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).get()
            val invoices = invoiceRepository.findAllByParkingLot(parkingLot.id, pageable)
            return ResponseEntity.ok(ApiResponse(invoices, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
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
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            invoiceService.createInvoice(parkingLot.parkingLotUuid, vehicleUuid, req.estimatedParkingDurationInHours)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR_CREATE.name, null))))
        }
    }

    /**
     * End an invoice
     */
    @PostMapping("/end/{invoiceId}")
    fun endInvoice(
        @PathVariable invoiceId: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            invoiceService.endInvoice(parkingLot.parkingLotUuid, invoiceId)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }

    /**
     * Pay an invoice
     */
    @PostMapping("/pay/{invoiceId}")
    fun payInvoice(
        @PathVariable invoiceId: String,
        principal: Principal
    ): ResponseEntity<ApiResponse<Nothing>> {
        return try {
            val user = userRepository.findByUserUuid(principal.name).get()
            val parkingLot = parkingLotRepository.findByOwner(user.id).get()
            invoiceService.payInvoice(parkingLot.parkingLotUuid, invoiceId)
            ResponseEntity.ok(ApiResponse(null, listOf()))
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.badRequest()
                .body(ApiResponse(null, listOf(ErrorElement(ResponseNames.ERROR.name, null))))
        }
    }
}