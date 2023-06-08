package com.chatchatabc.parking.domain.specification;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecification<T> {
    public static <T> Specification<T> notVerified() {
        return (root, query, builder) -> builder.isNull(root.get("verifiedAt"));
    }

    public static <T> Specification<T> verified() {
        return (root, query, builder) -> builder.isNotNull(root.get("verifiedAt"));
    }

    public static <T> Specification<T> sortBy(String field, Integer sortBy) {
        return (root, query, builder) -> {
            if (sortBy == 1) {
                query.orderBy(builder.asc(root.get(field)));
            } else {
                query.orderBy(builder.desc(root.get(field)));
            }
            return query.getRestriction();
        };
    }

}
