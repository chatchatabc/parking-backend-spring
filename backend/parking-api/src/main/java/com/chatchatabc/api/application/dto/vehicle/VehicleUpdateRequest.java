package com.chatchatabc.api.application.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleUpdateRequest implements Serializable {
    private String name;
    private String plateNumber;
    private Integer type;
}
