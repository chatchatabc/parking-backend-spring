package com.chatchatabc.api.application.dto.report;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse implements Serializable {
    private ReportDTO report;
    private ErrorContent error;
}
