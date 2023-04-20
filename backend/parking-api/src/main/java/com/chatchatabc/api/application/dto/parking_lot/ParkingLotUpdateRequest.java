package com.chatchatabc.api.application.dto.parking_lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotUpdateRequest implements Serializable {
    private String name;
    private BigDecimal rate;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
}
