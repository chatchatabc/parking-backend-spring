package com.chatchatabc.parking.application.dto.invoice;

import com.chatchatabc.parking.application.dto.ErrorContent;
import com.chatchatabc.parking.domain.model.Invoice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceResponse implements Serializable {
    private Invoice invoice;
    private ErrorContent error;
}
