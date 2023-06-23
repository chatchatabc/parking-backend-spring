package com.chatchatabc.parking.web.common.application.rest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@Service
public interface GpsRestService {
    record AzliotLoginResponse(
            AzliotTokenResult results
    ) {
    }

    record AzliotTokenResult(
            String token
    ) {
    }

    /**
     * Generate token
     *
     * @return AzliotLoginResponse
     */
    @GetExchange("/Transfer/transferLogin")
    AzliotLoginResponse generateToken(
            @RequestParam("tfKey") String tfKey,
            @RequestParam("userKey") String userKey,
            @RequestParam("password") String password,
            @RequestParam("time") String time
    );

    /**
     * Car Home TF Response record
     *
     * @param results
     */
    record CarHomeTFResponse(
            List<CarResultsItem> results
    ) {
    }

    /**
     * Car Result Item record
     *
     * @param carNum
     * @param groupName
     * @param vkey
     * @param deviceId
     * @param lat
     * @param lng
     * @param dir
     */
    record CarResultsItem(
            String carNum,
            String groupName,
            String vkey,
            String deviceId,
            Double lat,
            Double lng,
            Integer dir
    ) {
    }

    /**
     * Get Car Home TF
     *
     * @return CarHomeTFResponse
     */
    @GetExchange("/Transfer/carHomeTF")
    CarHomeTFResponse getCarHomeTF();

}
