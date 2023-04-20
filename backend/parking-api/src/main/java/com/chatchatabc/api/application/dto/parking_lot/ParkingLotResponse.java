package com.chatchatabc.api.application.dto.parking_lot;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotResponse implements Serializable {
    private ParkingLotDTO parkingLot;
    private ErrorContent error;
}
