package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.Rate;

public interface RateService {

    /**
     * Save rate
     *
     * @param rate the rate to be saved
     * @return the saved rate
     */
    Rate saveRate(Rate rate);
}
