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
    void countActiveInvoicesByParkingLotId() {
    }

    @Test
    void findAllByParkingLot() {
    }

    @Test
    void countActiveInvoicesByVehicle() {
    }
}