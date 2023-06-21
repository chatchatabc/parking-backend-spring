package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.VehicleBrand;
import org.springframework.data.jpa.domain.Specification;

public class VehicleBrandSpecification extends GenericSpecification<VehicleBrand> {
    public static Specification<VehicleBrand> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
