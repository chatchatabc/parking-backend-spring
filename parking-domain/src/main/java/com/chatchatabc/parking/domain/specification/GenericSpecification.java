package com.chatchatabc.parking.domain.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GenericSpecification<T> implements Specification<T> {
    private record Criteria(String key, String operation, String value) {
    }

    private final List<Criteria> params = new ArrayList<>();

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

    public GenericSpecification<T> withParams(Map<String, Object> parameters) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            String operation = "like";

            // If key is == status
            if (Objects.equals(entry.getKey(), "status")) {
                operation = "eq";
            }

            params.add(new Criteria(entry.getKey(), operation, entry.getValue().toString()));
        }
        return this;
    }

    @Override
    public Predicate toPredicate(@NotNull Root<T> root, @NotNull CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate predicate = cb.conjunction();

        for (Criteria param : params) {
            System.out.println(param.key() + " " + param.operation() + " " + param.value());
            try {
                switch (param.operation()) {
                    case "like" ->
                            predicate = cb.and(predicate, cb.like(root.get(param.key()), "%" + param.value() + "%"));
                    case "eq" -> predicate = cb.and(predicate, cb.equal(root.get(param.key()), param.value()));
                    case "gt" -> predicate = cb.and(predicate, cb.greaterThan(root.get(param.key()), param.value()));
                    case "lt" -> predicate = cb.and(predicate, cb.lessThan(root.get(param.key()), param.value()));
                    case "gte" ->
                            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get(param.key()), param.value()));
                    case "lte" ->
                            predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get(param.key()), param.value()));
                }
            } catch (Exception ignored) {
            }
        }

        return predicate;
    }
}
