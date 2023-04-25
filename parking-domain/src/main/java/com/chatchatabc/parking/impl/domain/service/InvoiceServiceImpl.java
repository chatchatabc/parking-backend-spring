package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotId the parking lot id
     * @param vehicleId    the vehicle id
     * @return the invoice
     */
    @Override
    public Invoice createInvoice(String parkingLotId, String vehicleId) throws Exception {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }

        // Check if vehicle has active invoice on this parking lot and return an error
        Long activeInvoices = invoiceRepository.countActiveInvoicesByVehicle(parkingLot.get(), vehicle.get());
        if (activeInvoices > 0) {
            throw new Exception("Vehicle has active invoice on this parking lot");
        }
        Invoice invoice = new Invoice();
        invoice.setParkingLot(parkingLot.get());
        invoice.setVehicle(vehicle.get());
        invoice.setStartAt(LocalDateTime.now());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        parkingLot.get().setCapacity(parkingLot.get().getCapacity() - 1);
        parkingLotRepository.save(parkingLot.get());
        // TODO: Sent NATS notification to update capacity of parking lot
        return savedInvoice;
    }

    /**
     * End invoice
     *
     * @param invoiceId    the invoice id
     * @param parkingLotId the parking lot id
     * @return the invoice
     */
    @Override
    public Invoice endInvoice(String invoiceId, String parkingLotId) throws Exception {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if (invoice.isEmpty()) {
            throw new Exception("Invoice not found");
        }
        if (invoice.get().getEndAt() != null) {
            throw new Exception("Invoice has already ended");
        }
        invoice.get().setEndAt(LocalDateTime.now());
        // Calculate total cost based on rate and start/end time
        // TODO: Use formula from rate to calculate total cost
        Duration duration = Duration.between(invoice.get().getStartAt(), invoice.get().getEndAt());
        Long hours = duration.toHours();
        // TODO: Calculate rate based on rate formula
        Invoice savedInvoice = invoiceRepository.save(invoice.get());
        // Update parking lot capacity
        parkingLot.get().setCapacity(parkingLot.get().getCapacity() + 1);
        parkingLotRepository.save(parkingLot.get());
        // TODO: Nats notification to update capacity of parking lot
        return savedInvoice;
    }

    /**
     * Pay invoice
     *
     * @param invoiceId    the invoice id
     * @param parkingLotId the parking lot id
     * @return the invoice
     */
    @Override
    public Invoice payInvoice(String invoiceId, String parkingLotId) throws Exception {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(parkingLotId);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);
        if (invoice.isEmpty()) {
            throw new Exception("Invoice not found");
        }
        if (invoice.get().getEndAt() == null) {
            throw new Exception("Invoice has not ended");
        }
        if (invoice.get().getPaidAt() != null) {
            throw new Exception("Invoice has already paid");
        }
        invoice.get().setPaidAt(LocalDateTime.now());
        // TODO: Send NATS notification to update invoice status
        return invoiceRepository.save(invoice.get());
    }
}
