package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void testFindAllByVehicle_InvoicesAreFound() {
        Long vehicleId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicle(vehicleId, pageRequest).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByVehicle_InvoicesAreNotFound() {
        Long vehicleId = 3L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicle(vehicleId, pageRequest).getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void testCountActiveInvoices_ShouldReturnGreaterThan0() {
        assertThat(invoiceRepository.countActiveInvoices()).isGreaterThan(0L);
    }

    @Test
    void testCountActiveInvoicesByParkingLotId_ShouldReturnGreaterThan0() {
        Long parkingLotId = 1L;
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotId(parkingLotId)).isGreaterThan(0L);
    }

    @Test
    void testCountActiveInvoicesByParkingLotId_ShouldReturn0() {
        Long parkingLotId = 4L;
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotId(parkingLotId)).isEqualTo(0L);
    }

    @Test
    void testFindAllByParkingLot_ShouldContainInvoices() {
        Long parkingLotId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByParkingLot(parkingLotId, pageRequest).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void countActiveInvoicesByVehicle() {
    }
}