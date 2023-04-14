package com.chatchatabc.parking.domain.repository

import com.chatchatabc.parking.domain.model.Invoice
import com.chatchatabc.parking.domain.model.ParkingLot
import com.chatchatabc.parking.domain.model.Vehicle
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InvoiceRepository : JpaRepository<Invoice, String> {

    /**
     * Find an invoice by id and parking lot
     */
    @Query("SELECT i FROM Invoice i WHERE i.id = ?1 AND i.parkingLot = ?2")
    fun findByIdAndParkingLot(invoiceId: String, parkingLot: ParkingLot): Optional<Invoice>

    /**
     * Find all invoices by vehicle
     */
    @Query("SELECT i FROM Invoice i WHERE i.vehicle = ?1")
    fun findAllByVehicle(vehicle: Vehicle, pageable: Pageable): Page<Invoice>

    /**
     * Find all invoices by parking lot
     */
    @Query("SELECT i FROM Invoice i WHERE i.parkingLot = ?1")
    fun findAllByParkingLot(parkingLot: ParkingLot, pageable: Pageable): Page<Invoice>

    /**
     * Find all active invoices by parking lot and by vehicle
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLot = ?1 AND i.vehicle = ?2 AND i.endAt IS NULL")
    fun countActiveInvoicesByVehicle(parkingLotId: String, vehicle: Vehicle): Long
}