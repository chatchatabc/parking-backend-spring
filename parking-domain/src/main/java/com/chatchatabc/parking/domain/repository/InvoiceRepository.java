package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.domain.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    /**
     * Find invoice by uuid
     *
     * @param invoiceUuid the invoice uuid
     * @return the invoice
     */
    Optional<Invoice> findByInvoiceUuid(String invoiceUuid);

    /**
     * Find invoice by vehicle
     *
     * @param vehicleUuid the vehicle uuid
     * @param pageable    the pageable
     * @return pages of invoice
     */
    @Query("SELECT i FROM Invoice i WHERE i.vehicleUuid = ?1")
    Page<Invoice> findAllByVehicle(String vehicleUuid, Pageable pageable);

    /**
     * Count active invoices
     *
     * @return the number of active invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.endAt IS NULL")
    Long countActiveInvoices();

    /**
     * Count active invoices by parking lot uuid
     *
     * @param parkingLotUuid the parking lot uuid
     * @return the number of active invoices
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLotUuid = ?1 AND i.endAt IS NULL")
    Long countActiveInvoicesByParkingLotUuid(String parkingLotUuid);

    /**
     * Find all invoices by parking lot
     */
    @Query("SELECT i FROM Invoice i WHERE i.parkingLotUuid = ?1")
    Page<Invoice> findAllByParkingLot(String parkingLotUuid, Pageable pageable);

    /**
     * Find all active invoices by parking lot and by vehicle
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.parkingLotUuid = ?1 AND i.vehicleUuid = ?2 AND i.endAt IS NULL")
    Long countActiveInvoicesByParkingLotAndVehicle(String parkingLotUuid, String vehicle);

    /**
     * Find latest active invoice
     *
     * @param parkingLotUuid the parking lot uuid
     * @param vehicleUuid    the vehicle uuid
     * @return the latest active invoice
     */
    @Query("SELECT i FROM Invoice i WHERE i.parkingLotUuid = ?1 AND i.vehicleUuid = ?2 AND i.endAt IS NULL ORDER BY i.createdAt DESC LIMIT 1")
    Optional<Invoice> findLatestActiveInvoice(String parkingLotUuid, String vehicleUuid);

    /**
     * Find all invoices by parking lot and by vehicle
     *
     * @param vehicleUuids the vehicle uuids
     * @param pageable     the pageable
     * @return pages of invoice
     */
    @Query("SELECT i FROM Invoice i WHERE i.vehicleUuid IN ?1")
    Page<Invoice> findAllByVehicles(List<String> vehicleUuids, Pageable pageable);
}
