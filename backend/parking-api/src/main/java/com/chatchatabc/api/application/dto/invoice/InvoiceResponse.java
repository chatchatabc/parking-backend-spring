package com.chatchatabc.api.application.dto.invoice;

import com.chatchatabc.api.application.dto.ErrorContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse implements Serializable {
    private InvoiceDTO invoice;
    private ErrorContent error;
}
