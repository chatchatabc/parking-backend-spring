package com.chatchatabc.api.application.dto.vehicle;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleResponse implements Serializable {
    private VehicleDTO vehicle;
    private ErrorContent error;
}
