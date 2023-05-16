package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingLotService {

    /**
     * Register a new parking lot
     *
     * @param ownerId            the owner of the parking lot
     * @param name               the name of the parking lot
     * @param latitude           the latitude of the parking lot
     * @param longitude          the longitude of the parking lot
     * @param address            the address of the parking lot
     * @param description        the description of the parking lot
     * @param capacity           the capacity of the parking lot
     * @param businessHoursStart the business hours start of the parking lot
     * @param businessHoursEnd   the business hours end of the parking lot
     * @param openDaysFlag       the open days flag of the parking lot
     * @return the parking lot
     */
    ParkingLot registerParkingLot(
            String ownerId,
            String name,
            Double latitude,
            Double longitude,
            String address,
            String description,
            Integer capacity,
            LocalDateTime businessHoursStart,
            LocalDateTime businessHoursEnd,
            Integer openDaysFlag
    ) throws Exception;

    /**
     * Update parking lot
     *
     * @param memberId           the member id
     * @param parkingLotUuid     the parking lot uuid
     * @param name               the name of the parking lot
     * @param latitude           the latitude of the parking lot
     * @param longitude          the longitude of the parking lot
     * @param address            the address of the parking lot
     * @param description        the description of the parking lot
     * @param capacity           the capacity of the parking lot
     * @param businessHoursStart the business hours start of the parking lot
     * @param businessHoursEnd   the business hours end of the parking lot
     * @param openDaysFlag       the open days flag of the parking lot
     * @return the parking lot
     */
    ParkingLot updateParkingLot(
            String memberId,
            String parkingLotUuid,
            String name,
            Double latitude,
            Double longitude,
            String address,
            String description,
            Integer capacity,
            LocalDateTime businessHoursStart,
            LocalDateTime businessHoursEnd,
            Integer openDaysFlag,
            List<ParkingLotImage> images
    ) throws Exception;

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
