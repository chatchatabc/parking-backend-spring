package com.chatchatabc.parking.application.dto.parking_lot;

import com.chatchatabc.parking.application.dto.ErrorContent;
import com.chatchatabc.parking.domain.model.ParkingLot;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotResponse implements Serializable {
    private ParkingLot parkingLot;
    private ErrorContent error;
}
