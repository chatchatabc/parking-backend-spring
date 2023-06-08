package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.Invoice;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceSpecification extends GenericSpecification<Invoice> {
    public static Specification<Invoice> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("invoiceUuid")), pattern),
                    builder.like(builder.lower(root.get("parkingLotUuid")), pattern),
                    builder.like(builder.lower(root.get("vehicleUuid")), pattern)
            );
        };
    }
}
