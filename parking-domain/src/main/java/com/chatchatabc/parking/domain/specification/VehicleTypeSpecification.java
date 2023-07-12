package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.VehicleType;
import org.springframework.data.jpa.domain.Specification;

public class VehicleTypeSpecification extends GenericSpecification<VehicleType> {
    public Specification<VehicleType> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
