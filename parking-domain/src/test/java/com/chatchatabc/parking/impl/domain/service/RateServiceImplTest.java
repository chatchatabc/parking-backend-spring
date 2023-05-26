package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.TestContainersBaseTest;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Rate;
import com.chatchatabc.parking.domain.repository.ParkingLotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

class RateServiceImplTest extends TestContainersBaseTest {
    @Autowired
    private RateServiceImpl rateService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Test
    void testSaveRate_ShouldSaveSuccessfully() {
        ParkingLot parkingLot = parkingLotRepository.findById(1L).orElseThrow();
        Rate rate = new Rate();
        rate.setParkingLot(parkingLot);
        rate.setType(0);
        rate.setInterval(0);
        rate.setFreeHours(0);
        rate.setPayForFreeHoursWhenExceeding(true);
        rate.setStartingRate(new BigDecimal(0));
        rate.setRate(new BigDecimal(0));
        rateService.saveRate(rate);
    }
}