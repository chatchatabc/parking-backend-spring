package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.Jeepney;
import org.springframework.data.jpa.domain.Specification;

public class JeepneySpecification extends GenericSpecification<Jeepney> {
    public static Specification<Jeepney> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("plateNumber")), pattern),
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
