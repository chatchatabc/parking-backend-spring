package com.chatchatabc.parking.application.dto.report;

import com.chatchatabc.parking.application.dto.ErrorContent;
import com.chatchatabc.parking.domain.model.Report;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse implements Serializable {
    private Report report;
    private ErrorContent error;
}
