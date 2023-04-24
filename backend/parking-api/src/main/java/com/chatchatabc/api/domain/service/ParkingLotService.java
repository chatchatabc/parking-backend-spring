package com.chatchatabc.api.domain.service;

import com.chatchatabc.api.application.dto.parking_lot.ParkingLotDTO;

public interface ParkingLotService {
    ParkingLotDTO registerParkingLot(
            String ownerId,
            String name,
            Double latitude,
            Double longitude,
            String address,
            String description,
            Integer capacity
    );
}
