package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Rate;
import com.chatchatabc.parking.domain.repository.RateRepository;
import com.chatchatabc.parking.domain.service.RateService;
import org.springframework.stereotype.Service;

@Service
public class RateServiceImpl implements RateService {
    private final RateRepository rateRepository;

    public RateServiceImpl(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    /**
     * Save rate
     *
     * @param rate the rate to be saved
     * @return the saved rate
     */
    @Override
    public Rate saveRate(Rate rate) {
        return rateRepository.save(rate);
    }
}