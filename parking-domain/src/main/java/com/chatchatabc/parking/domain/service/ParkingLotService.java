package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.ParkingLot;

public interface ParkingLotService {

    /**
     * Save parking lot
     *
     * @param parkingLot the parking lot
     */
    void saveParkingLot(ParkingLot parkingLot);

    /**
     * Verify parking lot
     *
     * @param memberId       the member id
     * @param parkingLotUuid the parking lot uuid
     * @return the parking lot
     */
    ParkingLot verifyParkingLot(
            String memberId,
            String parkingLotUuid
    ) throws Exception;
}
