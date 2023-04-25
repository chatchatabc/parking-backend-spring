package com.chatchatabc.parking.impl.infra.service;

import com.chatchatabc.parking.infra.service.UtilService;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class UtilServiceImpl implements UtilService {
    private final Random random = new Random();

    /**
     * Generate OTP
     *
     * @return OTP
     */
    @Override
    public String generateOTP() {
        int randomNumber = random.nextInt(1000000);
        return String.format(Locale.ENGLISH, "%06d", randomNumber);
    }
}
