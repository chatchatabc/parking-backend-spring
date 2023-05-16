package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.Rate;

import java.math.BigDecimal;

public interface RateService {

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
    Rate createRate(ParkingLot parkingLot, Integer type, Integer interval, Integer freeHours, Boolean payForFreeHoursWhenExceeding, BigDecimal startingRate, BigDecimal rate);

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
    Rate updateRate(String rateId, Integer type, Integer interval, Integer freeHours, Boolean payForFreeHoursWhenExceeding, BigDecimal startingRate, BigDecimal rate) throws Exception;
}
