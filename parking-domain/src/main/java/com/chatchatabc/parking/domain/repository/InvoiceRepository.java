package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    /**
     * Find invoice by vehicle
     *
     * @param vehicleId the vehicle id
     * @param pageable  the pageable
     * @return pages of invoice
     */
    @Query("SELECT i FROM Invoice i WHERE i.vehicle = ?1")
    Page<Invoice> findAllByVehicle(Long vehicleId, Pageable pageable);

    /**
     * Count active invoices
     *
     * @return the number of active invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.endAt IS NULL")
    Long countActiveInvoices();

    /**
     * Count active invoices by parking lot id
     *
     * @param parkingLotId the parking lot id
     * @return the number of active invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLot = ?1 AND i.endAt IS NULL")
    Long countActiveInvoicesByParkingLotId(Long parkingLotId);

    /**
     * Find all invoices by parking lot
     */
    @Query("SELECT i FROM Invoice i WHERE i.parkingLot = ?1")
    Page<Invoice> findAllByParkingLot(Long parkingLot, Pageable pageable);

    /**
     * Find all active invoices by parking lot and by vehicle
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLot = ?1 AND i.vehicle = ?2 AND i.endAt IS NULL")
    Long countActiveInvoicesByParkingLotAndVehicle(Long parkingLot, Long vehicle);
}
