package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.Vehicle;
import org.springframework.data.jpa.domain.Specification;

public class VehicleSpecification extends GenericSpecification<Vehicle> {
    public Specification<Vehicle> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("plateNumber")), pattern),
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
