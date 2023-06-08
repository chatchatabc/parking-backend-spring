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
        ParkingLot parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).orElseThrow();
        Vehicle vehicle = vehicleRepository.findByVehicleUuid(vehicleUuid).orElseThrow();

        if (parkingLot.getRate() == null) {
            throw new Exception("Parking lot rate not found");
        }

        // Check if vehicle has active invoice on this parking lot and return an error
        Long activeInvoices = invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLot.getParkingLotUuid(), vehicle.getVehicleUuid());
        if (activeInvoices > 0) {
            throw new Exception("Vehicle has active invoice on this parking lot");
        }

        Invoice invoice = new Invoice();
        invoice.setParkingLotUuid(parkingLot.getParkingLotUuid());
        invoice.setVehicleUuid(vehicle.getVehicleUuid());
        invoice.setStartAt(LocalDateTime.now());
        invoice.setEstimatedParkingDurationInHours(estimatedParkingDurationInHours);

        Invoice savedInvoice = invoiceRepository.save(invoice);
        parkingLot.setCapacity(parkingLot.getCapacity() - 1);
        parkingLotRepository.save(parkingLot);
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
        if (rate == null) {
            throw new Exception("Parking lot rate not found");
        }

        // Calculate total cost based on rate and start/end time
        BigDecimal totalCost = this.calculateInvoice(invoice, parkingLot.getRate());

        invoice.setTotal(totalCost);
        invoiceRepository.save(invoice);
        // Update parking lot capacity
        parkingLot.setCapacity(parkingLot.getCapacity() + 1);
        parkingLotRepository.save(parkingLot);
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

    /**
     * Calculate invoice
     *
     * @param invoice the invoice
     * @param rate    the rate
     * @return the big decimal
     * @throws Exception the exception
     */
    @Override
    public BigDecimal calculateInvoice(Invoice invoice, Rate rate) throws Exception {
        // Initial values
        BigDecimal totalCost = BigDecimal.valueOf(0);

        LocalDateTime endAt = invoice.getEndAt();

        if (endAt == null) {
            endAt = LocalDateTime.now();
        }

        // Calculate rate based on rate formula
        Duration duration = Duration.between(invoice.getStartAt(), endAt);
        long hours = duration.toHours();

        // Add Starting Rate
        totalCost = totalCost.add(rate.getStartingRate());

        // Rate Type is Fixed
        if (rate.getType() == Rate.RateType.FIXED) {
            // Rate Interval is Hourly
            if (rate.getInterval() == Rate.RateInterval.HOURLY) {
                // Add Free Hours to Cost if isPayForFreeHoursWhenExceeding is true
                if (rate.isPayForFreeHoursWhenExceeding()) {
                    // If hours did not exceed free hours, set hours to 0
                    if (hours <= rate.getFreeHours()) {
                        hours = 0L;
                    }
                    // else, hours is counted on calculation of fee
                }
                // Deduct Free Hours if isPayForFreeHoursWhenExceeding is false
                else {
                    hours -= rate.getFreeHours();
                    // Clamp to 0 hours if less than 0
                    if (hours < 0) {
                        hours = 0L;
                    }
                }
                totalCost = totalCost.add(rate.getRate().multiply(BigDecimal.valueOf(hours)));
            }
            // Rate Interval is Daily
            else if (rate.getInterval() == Rate.RateInterval.DAILY) {
                // Calculate number of days
                long days = hours / 24;
                // Add 1 day if hours is not divisible by 24
                if (hours % 24 != 0) {
                    days += 1;
                }
                totalCost = totalCost.add(rate.getRate().multiply(BigDecimal.valueOf(days)));
            }
        }
        // Rate Type is Flexible
        else if (rate.getType() == Rate.RateType.FLEXIBLE) {
            // TODO: Implement Feature
        }

        return totalCost;
    }
}
