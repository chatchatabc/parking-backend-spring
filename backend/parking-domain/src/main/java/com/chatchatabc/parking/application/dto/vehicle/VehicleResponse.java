package com.chatchatabc.parking.application.dto.vehicle;

import com.chatchatabc.parking.application.dto.ErrorContent;
import com.chatchatabc.parking.domain.model.Vehicle;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse implements Serializable {
    private Vehicle vehicle;
    private ErrorContent error;
}
