package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Invoice;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    /**
     * Find invoice by id and parking lot
     *
     * @param id         the invoice id
     * @param parkingLot the parking lot
     * @return the invoice
     */
    Optional<Invoice> findByIdAndParkingLot(String id, ParkingLot parkingLot);

    /**
     * Find invoice by vehicle
     *
     * @param vehicle  the vehicle
     * @param pageable the pageable
     * @return pages of invoice
     */
    @Query("SELECT i FROM Invoice i WHERE i.vehicle = ?1")
    Page<Invoice> findAllByVehicle(Vehicle vehicle, Pageable pageable);

    /**
     * Count active invoices
     *
     * @param parkingLotId the parking lot id
     * @return the number of active invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLot.id = ?1 AND i.endAt IS NULL")
    Long countActiveInvoices(String parkingLotId);

    /**
     * Find all invoices by parking lot
     */
    @Query("SELECT i FROM Invoice i WHERE i.parkingLot = ?1")
    Page<Invoice> findAllByParkingLot(ParkingLot parkingLot, Pageable pageable);

    /**
     * Find all active invoices by parking lot and by vehicle
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLot = ?1 AND i.vehicle = ?2 AND i.endAt IS NULL")
    Long countActiveInvoicesByVehicle(ParkingLot parkingLot, Vehicle vehicle);
}
