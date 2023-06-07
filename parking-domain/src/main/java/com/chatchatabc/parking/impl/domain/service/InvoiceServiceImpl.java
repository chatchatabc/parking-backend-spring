package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Invoice;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Rate;
import com.chatchatabc.parking.domain.model.Vehicle;
import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import com.chatchatabc.parking.domain.repository.VehicleRepository;
import com.chatchatabc.parking.domain.service.InvoiceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final ParkingLotRepository parkingLotRepository;
    private final VehicleRepository vehicleRepository;
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(ParkingLotRepository parkingLotRepository, VehicleRepository vehicleRepository, InvoiceRepository invoiceRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.vehicleRepository = vehicleRepository;
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Create invoice for vehicle
     *
     * @param parkingLotUuid                  the parking lot uuid
     * @param vehicleUuid                     the vehicle uuid
     * @param estimatedParkingDurationInHours the estimated parking duration in hours
     */
    @Override
    public Invoice createInvoice(String parkingLotUuid, String vehicleUuid, Integer estimatedParkingDurationInHours) throws Exception {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        Optional<Vehicle> vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid);
        if (vehicle.isEmpty()) {
            throw new Exception("Vehicle not found");
        }

//        if (parkingLot.get().getRate() == null) {
//            throw new Exception("Parking lot rate not found");
//        }

        // Check if vehicle has active invoice on this parking lot and return an error
        Long activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLot.get().getParkingLotUuid(), vehicle.get().getVehicleUuid());
        if (activeInvoices > 0) {
            throw new Exception("Vehicle has active invoice on this parking lot");
        }
        Invoice invoice = new Invoice();
        invoice.setParkingLotUuid(parkingLot.get().getParkingLotUuid());
        invoice.setVehicleUuid(vehicle.get().getVehicleUuid());
        invoice.setStartAt(LocalDateTime.now());
        invoice.setEstimatedParkingDurationInHours(estimatedParkingDurationInHours);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        parkingLot.get().setCapacity(parkingLot.get().getCapacity() - 1);
        parkingLotRepository.save(parkingLot.get());
        // TODO: Sent NATS notification to update capacity of parking lot
        return savedInvoice;
    }

    /**
     * End invoice
     *
     * @param invoiceUuid    the invoice uuid
     * @param parkingLotUuid the parking lot uuid
     */
    @Override
    public void endInvoice(String invoiceUuid, String parkingLotUuid) throws Exception {
        ParkingLot parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).orElseThrow();
        Invoice invoice = invoiceRepository.findByInvoiceUuid(invoiceUuid).orElseThrow();
        if (invoice.getEndAt() != null) {
            throw new Exception("Invoice has already ended");
        }
        invoice.setEndAt(LocalDateTime.now());

        Rate rate = parkingLot.getRate();
        BigDecimal totalCost = BigDecimal.valueOf(0);

        // Calculate total cost based on rate and start/end time
        // TODO: Use formula from rate to calculate total cost, round end at to nearest hour
        Duration duration = Duration.between(invoice.getStartAt(), invoice.getEndAt());
        Long hours = duration.toHours();
        // TODO: Calculate rate based on rate formula
        invoiceRepository.save(invoice);
        // Update parking lot capacity
        parkingLot.setCapacity(parkingLot.getCapacity() + 1);
        parkingLotRepository.save(parkingLot);
        // TODO: Nats notification to update capacity of parking lot
    }

    /**
     * Pay invoice
     *
     * @param invoiceUuid    the invoice uuid
     * @param parkingLotUuid the parking lot uuid
     */
    @Override
    public void payInvoice(String invoiceUuid, String parkingLotUuid) throws Exception {
        Optional<ParkingLot> parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid);
        if (parkingLot.isEmpty()) {
            throw new Exception("Parking lot not found");
        }
        Optional<Invoice> invoice = invoiceRepository.findByInvoiceUuid(invoiceUuid);
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
        invoiceRepository.save(invoice.get());
    }
}
