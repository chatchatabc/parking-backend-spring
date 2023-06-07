package com.chatchatabc.parking.admin.application.graphql

import com.chatchatabc.parking.admin.application.dto.PageInfo
import com.chatchatabc.parking.admin.application.dto.PagedResponse
import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.repository.InvoiceRepository
import com.chatchatabc.parking.domain.repository.ParkingLotRepository
import com.chatchatabc.parking.domain.repository.UserRepository
import com.chatchatabc.parking.domain.repository.VehicleRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.util.*

@Controller
class InvoiceGQLController(
    private val invoiceRepository: InvoiceRepository,
    private val vehicleRepository: VehicleRepository,
    private val parkingLotRepository: ParkingLotRepository,
    private val userRepository: UserRepository
) {
    /**
     * Get invoices
     */
    @QueryMapping
    fun getInvoices(
        @Argument page: Int,
        @Argument size: Int
    ): PagedResponse<Invoice> {
        val pr = PageRequest.of(page, size)
        val invoices = invoiceRepository.findAll(pr)
        return PagedResponse(
            invoices.content,
            PageInfo(
                invoices.size,
                invoices.totalElements,
                invoices.isFirst,
                invoices.isLast,
                invoices.isEmpty
            )
        )
    }

    /**
     * Get invoice by uuid
     */
    @QueryMapping
    fun getInvoiceByUuid(
        @Argument uuid: String
    ): Optional<Invoice> {
        return invoiceRepository.findByInvoiceUuid(uuid)
    }

    /**
     * Get invoices by vehicle uuid
     */
    @QueryMapping
    fun getInvoicesByVehicleUuid(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): PagedResponse<Invoice> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        val vehicle = vehicleRepository.findByVehicleUuid(uuid).get()
        val invoices = invoiceRepository.findAllByVehicle(vehicle.vehicleUuid, pr)
        return PagedResponse(
            invoices.content,
            PageInfo(
                invoices.size,
                invoices.totalElements,
                invoices.isFirst,
                invoices.isLast,
                invoices.isEmpty
            )
        )
    }

    /**
     * Get invoices by parking lot uuid
     */
    @QueryMapping
    fun getInvoicesByParkingLotUuid(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): PagedResponse<Invoice> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        val parkingLot = parkingLotRepository.findByParkingLotUuid(uuid).get()
        val invoices = invoiceRepository.findAllByParkingLot(parkingLot.parkingLotUuid, pr)
        return PagedResponse(
            invoices.content,
            PageInfo(
                invoices.size,
                invoices.totalElements,
                invoices.isFirst,
                invoices.isLast,
                invoices.isEmpty
            )
        )
    }

    /**
     * Get invoices by user uuid
     */
    @QueryMapping
    fun getInvoicesByUserUuid(
        @Argument page: Int,
        @Argument size: Int,
        @Argument uuid: String
    ): PagedResponse<Invoice> {
        val sort = Sort.by(Sort.Direction.DESC, "createdAt")
        val pr = PageRequest.of(page, size, sort)
        val user = userRepository.findByUserUuid(uuid).get()
        val vehicles = vehicleRepository.findVehicleIdsByOwner(user.id)
        val invoices = invoiceRepository.findAllByVehicles(vehicles, pr)
        return PagedResponse(
            invoices.content,
            PageInfo(
                invoices.size,
                invoices.totalElements,
                invoices.isFirst,
                invoices.isLast,
                invoices.isEmpty
            )
        )
    }
}