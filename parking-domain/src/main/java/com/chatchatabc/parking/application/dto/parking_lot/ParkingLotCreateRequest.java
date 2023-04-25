package com.chatchatabc.parking.application.dto.parking_lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotCreateRequest implements Serializable {
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private Integer capacity;
}
