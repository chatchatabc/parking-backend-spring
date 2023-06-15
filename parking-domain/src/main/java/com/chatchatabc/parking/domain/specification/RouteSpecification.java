package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.Route;
import org.springframework.data.jpa.domain.Specification;

public class RouteSpecification extends GenericSpecification<Route> {
    public static Specification<Route> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern)
            );
        };
    }
}
