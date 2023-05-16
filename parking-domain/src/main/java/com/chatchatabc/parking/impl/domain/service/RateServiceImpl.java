package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Rate;
import com.chatchatabc.parking.domain.repository.RateRepository;
import com.chatchatabc.parking.domain.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RateServiceImpl implements RateService {
    @Autowired
    RateRepository rateRepository;

    /**
     * Create a new rate
     *
     * @param parkingLot                   the parking lot
     * @param type                         the type of rate
     * @param interval                     the interval of the rate
     * @param freeHours                    the free hours of the rate
     * @param payForFreeHoursWhenExceeding the pay for free hours when exceeding of the rate
     * @param startingRate                 the starting rate of the rate
     * @param rate                         the rate
     * @return the rate
     */
    @Override
    public Rate createRate(ParkingLot parkingLot, Integer type, Integer interval, Integer freeHours, Boolean payForFreeHoursWhenExceeding, BigDecimal startingRate, BigDecimal rate) {
        Rate createdRate = new Rate();
        createdRate.setType(type);
        createdRate.setParkingLot(parkingLot);
        createdRate.setInterval(interval);
        createdRate.setFreeHours(freeHours);
        createdRate.setPayForFreeHoursWhenExceeding(payForFreeHoursWhenExceeding);
        createdRate.setStartingRate(startingRate);
        createdRate.setRate(rate);
        return createdRate;
    }

    /**
     * Update rate
     *
     * @param rateId                       the rate id
     * @param type                         the type of rate
     * @param interval                     the interval of the rate
     * @param freeHours                    the free hours of the rate
     * @param payForFreeHoursWhenExceeding the pay for free hours when exceeding of the rate
     * @param startingRate                 the starting rate of the rate
     * @param rate                         the rate
     * @return the rate
     */
    @Override
    public Rate updateRate(String rateId, Integer type, Integer interval, Integer freeHours, Boolean payForFreeHoursWhenExceeding, BigDecimal startingRate, BigDecimal rate) throws Exception {
        Optional<Rate> rateToUpdate = rateRepository.findById(rateId);
        if (rateToUpdate.isEmpty()) {
            throw new Exception("Rate not found");
        }

        // Apply updates
        if (type != null) {
            rateToUpdate.get().setType(type);
        }
        if (interval != null) {
            rateToUpdate.get().setInterval(interval);
        }
        if (freeHours != null) {
            rateToUpdate.get().setFreeHours(freeHours);
        }
        if (payForFreeHoursWhenExceeding != null) {
            rateToUpdate.get().setPayForFreeHoursWhenExceeding(payForFreeHoursWhenExceeding);
        }
        if (startingRate != null) {
            rateToUpdate.get().setStartingRate(startingRate);
        }
        if (rate != null) {
            rateToUpdate.get().setRate(rate);
        }
        return rateRepository.save(rateToUpdate.get());
    }
}
