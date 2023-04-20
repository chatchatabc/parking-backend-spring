package com.chatchatabc.api.application.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportCreateRequest {
    private String name;
    private String description;
    private String plateNumber;
    private Double latitude;
    private Double longitude;
}
