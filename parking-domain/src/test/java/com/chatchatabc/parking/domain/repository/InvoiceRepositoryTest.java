package com.chatchatabc.parking.domain.repository;

import com.chatchatabc.parking.TestContainersBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InvoiceRepositoryTest extends TestContainersBaseTest {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    void testFindAllByVehicle_InvoicesAreFound() {
        // Vehicle 1
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicle(vehicleUuid, pageRequest).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByVehicle_InvoicesAreNotFound() {
        // Vehicle 3
        String vehicleUuid = "f8e5e1d2-4c3b-2a1f-0e9d-8c7b6a5f4e3d";
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicle(vehicleUuid, pageRequest).getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void testCountActiveInvoices_ShouldReturnGreaterThan0() {
        assertThat(invoiceRepository.countActiveInvoices()).isGreaterThan(0L);
    }

    @Test
    void testCountActiveInvoicesByParkingLotUuid_ShouldReturnGreaterThan0() {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotUuid(parkingLotUuid)).isGreaterThan(0L);
    }

    @Test
    void testCountActiveInvoicesByParkingLotUuid_ShouldReturn0() {
        // Parking Lot 4
        String parkingLotUuid = "b3c4d5e6-f7g8-h9i0-j1k2-l3m4n5o6p7q";
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotUuid(parkingLotUuid)).isEqualTo(0L);
    }

    @Test
    void testFindAllByParkingLot_ShouldContainInvoices() {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByParkingLot(parkingLotUuid, pageRequest).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByParkingLot_ShouldNotContainInvoices() {
        // Parking Lot 4
        String parkingLotUuid = "b3c4d5e6-f7g8-h9i0-j1k2-l3m4n5o6p7q";
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByParkingLot(parkingLotUuid, pageRequest).getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void testCountActiveInvoicesByParkingLotAndVehicle_ShouldContainInvoices() {
        // Parking Lot 3
        String parkingLotUuid = "a2b3c4d5-e6f7-g8h9-i0j1-k2l3m4n5o6p";
        // Vehicle 2
        String vehicleUuid = "ddd6ed87-3d29-4b87-890c-75a86f684278";
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLotUuid, vehicleUuid)).isGreaterThan(0L);
    }

    @Test
    void testCountActiveInvoicesByParkingLotAndVehicle_ShouldNotContainInvoices() {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        // Vehicle 1
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        assertThat(invoiceRepository.countActiveInvoicesByParkingLotAndVehicle(parkingLotUuid, vehicleUuid)).isGreaterThan(0L);
    }

    @Test
    void testFindLatestActiveInvoice_ShouldContainInvoice() {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        // Vehicle 1
        String vehicleUuid = "2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9";
        assertThat(invoiceRepository.findLatestActiveInvoice(parkingLotUuid, vehicleUuid)).isPresent();
    }

    @Test
    void testFindLatestActiveInvoice_ShouldNotContainInvoice() {
        // Parking Lot 1
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";
        // Vehicle 3
        String vehicleUuid = "f8e5e1d2-4c3b-2a1f-0e9d-8c7b6a5f4e3d";
        assertThat(invoiceRepository.findLatestActiveInvoice(parkingLotUuid, vehicleUuid)).isNotPresent();
    }

    @Test
    void testFindByInvoiceUuid_ShouldFindInvoice() {
        String invoiceUuid = "d189b0cc-e7bb-4ba6-8d84-3d2512e1e27f";
        assertThat(invoiceRepository.findByInvoiceUuid(invoiceUuid)).isPresent();
    }

    @Test
    void testFindByInvoiceUuid_ShouldNotFindInvoice() {
        String invoiceUuid = "non-existent-uuid";
        assertThat(invoiceRepository.findByInvoiceUuid(invoiceUuid)).isNotPresent();
    }

    @Test
    void testFindAllByVehicles_ShouldReturnGreaterThan0() {
        // Vehicle 1
        List<String> vehicleIds = List.of("2da0ddab-9e9d-45cb-a2a5-f6bff1765ea9");
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicles(vehicleIds, pageRequest).getNumberOfElements()).isGreaterThan(0);
    }

    @Test
    void testFindAllByVehicles_ShouldReturn0() {
        List<String> vehicleIds = List.of("non-existent-uuid");
        PageRequest pageRequest = PageRequest.of(0, 10);
        assertThat(invoiceRepository.findAllByVehicles(vehicleIds, pageRequest).getNumberOfElements()).isEqualTo(0);
    }

    @Test
    void testCountTrafficByDateRange_ShouldReturnGreaterThan0() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 5, 23, 59);
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";

        assertThat(invoiceRepository.countTrafficByDateRange(parkingLotUuid, start, end)).isGreaterThan(0L);
    }

    @Test
    void testCountTrafficByDateRange_ShouldReturn0() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 5, 23, 59);
        String parkingLotUuid = "non-existent-uuid";

        assertThat(invoiceRepository.countTrafficByDateRange(parkingLotUuid, start, end)).isEqualTo(0L);
    }

    @Test
    void testCountLeavingVehicles_ShouldReturnGreaterThan0() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 7, 23, 59);
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";

        assertThat(invoiceRepository.countLeavingVehicles(parkingLotUuid, start, end)).isGreaterThan(0L);
    }

    @Test
    void testCountLeavingVehicles_ShouldReturn0() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 7, 23, 59);
        String parkingLotUuid = "non-existent-uuid";

        assertThat(invoiceRepository.countLeavingVehicles(parkingLotUuid, start, end)).isEqualTo(0L);
    }

    @Test
    void testSumTotalByParkingLotUuidAndEndAtDateRange_ShouldReturnGreaterThan0() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 5, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 7, 23, 59);
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";

        assertThat(invoiceRepository.sumTotalByParkingLotUuidAndEndAtDateRange(parkingLotUuid, start, end)).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void testSumTotalByParkingLotUuidAndEndAtDateRange_ShouldReturnNull() {
        LocalDateTime start = LocalDateTime.of(2023, 5, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 5, 2, 23, 59);
        String parkingLotUuid = "fe5c1764-d192-4690-834e-c611f078dd57";

        assertThat(invoiceRepository.sumTotalByParkingLotUuidAndEndAtDateRange(parkingLotUuid, start, end)).isNull();
    }
}