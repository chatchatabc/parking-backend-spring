package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.Invoice;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Rate;
import com.chatchatabc.parking.domain.repository.InvoiceRepository;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InvoiceServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    void testCreateInvoice_WhenVehicleHasNoActiveInvoiceToParkingLot_ShouldCreateInvoice() throws Exception {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        // Vehicle 3
        String vehicleUuid = "f8e5e1d2-4c3b-2a1f-0e9d-8c7b6a5f4e3d";
        Long currentCount = invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLotUuid, vehicleUuid);
        invoiceService.createInvoice(parkingLotUuid, vehicleUuid, 0);
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLotUuid, vehicleUuid)).isGreaterThan(currentCount);
    }

    @Test
    void testCreateInvoice_WhenVehicleHasActiveInvoiceToParkingLot_ShouldThrowException() {
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        assertThrows(Exception.class, () -> invoiceService.createInvoice(parkingLotUuid, vehicleUuid, 0));
    }

    @Test
    void testEndInvoice_WhenInvoiceIsActive_ShouldEndSuccessfully() throws Exception {
        String invoiceUuid = "9e8f7c6b-5a4d-3e2f-1a0b-cd9e8f7a6b5c";
        String parkingLotUuid = "a2b3c4d5-e6f7-g8h9-i0j1-k2l3m4n5o6p";
        invoiceService.endInvoice(invoiceUuid, parkingLotUuid);
        assertThat(invoiceRepository.findByInvoiceUuid(invoiceUuid).orElseThrow().getEndAt()).isNotNull();
    }

    @Test
    void testEndInvoice_WhenInvoiceNotFound_ShouldThrowException() {
        String invoiceUuid = "non-existent-uuid";
        String parkingLotUuid = "a2b3c4d5-e6f7-g8h9-i0j1-k2l3m4n5o6p";
        assertThrows(Exception.class, () -> invoiceService.endInvoice(invoiceUuid, parkingLotUuid));
    }

    @Test
    void testEndInvoice_WhenInvoiceIsAlreadyEnded_ShouldThrowException() {
        String invoiceUuid = "8d2e1f3a-4b5c-6a7b-9c8d-e0f1a2d3b4c5";
        String parkingLotUuid = "9c45f764-b54d-4fb1-8aa0-293c7e73c9c1";
        assertThrows(Exception.class, () -> invoiceService.endInvoice(invoiceUuid, parkingLotUuid));
    }

    @Test
    void testPayInvoice_WhenInvoiceIsNotPaid_ShouldPaySuccessfully() throws Exception {
        String invoiceUuid = "8d2e1f3a-4b5c-6a7b-9c8d-e0f1a2d3b4c5";
        String parkingLotUuid = "9c45f764-b54d-4fb1-8aa0-293c7e73c9c1";
        invoiceService.payInvoice(invoiceUuid, parkingLotUuid);
        assertThat(invoiceRepository.findByInvoiceUuid(invoiceUuid).orElseThrow().getPaidAt()).isNotNull();
    }

    @Test
    void testPayInvoice_WhenInvoiceNotFound_ShouldThrowException() {
        String invoiceUuid = "non-existent-uuid";
        String parkingLotUuid = "9c45f764-b54d-4fb1-8aa0-293c7e73c9c1";
        assertThrows(Exception.class, () -> invoiceService.payInvoice(invoiceUuid, parkingLotUuid));
    }

    @Test
    void testPayInvoice_WhenInvoiceIsAlreadyPaid_ShouldThrowException() {
        String invoiceUuid = "d189b0cc-e7bb-4ba6-8d84-3d2512e1e27f";
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        assertThrows(Exception.class, () -> invoiceService.payInvoice(invoiceUuid, parkingLotUuid));
    }

    @Test
    void testCalculateInvoice_ShouldCalculateInvoice() throws Exception {
        String invoiceUuid = "d189b0cc-e7bb-4ba6-8d84-3d2512e1e27f";
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        BigDecimal expected = new BigDecimal("148.00");

        ParkingLot parkingLot = parkingLotRepository.findByParkingLotUuid(parkingLotUuid).orElseThrow();
        Invoice invoice = invoiceRepository.findByInvoiceUuid(invoiceUuid).orElseThrow();
        Rate rate = parkingLot.getRate();

        assertThat(invoiceService.calculateInvoice(invoice, rate)).isNotNull().isEqualTo(expected);
    }
}