package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.VehicleModel;
import org.springframework.data.jpa.domain.Specification;

public class VehicleModelSpecification extends GenericSpecification<VehicleModel> {
    public static Specification<VehicleModel> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
