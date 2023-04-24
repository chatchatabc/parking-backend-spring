package com.chatchatabc.api.application.dto.parking_lot;

import com.chatchatabc.api.application.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotDTO implements Serializable {
    private String id;
    private UserDTO owner;
    // TODO: Add Rate
    private String name;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private Integer capacity;
    private Integer availableSlots;
    // TODO: Add more fields
}
