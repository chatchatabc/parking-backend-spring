package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> notVerified() {
        return (root, query, builder) -> builder.isNull(root.get("verifiedAt"));
    }

    public static Specification<User> verified() {
        return (root, query, builder) -> builder.isNotNull(root.get("verifiedAt"));
    }

    public static Specification<User> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("username")), pattern),
                    builder.like(builder.lower(root.get("email")), pattern),
                    builder.like(builder.lower(root.get("firstName")), pattern),
                    builder.like(builder.lower(root.get("lastName")), pattern),
                    builder.like(builder.lower(root.get("phone")), pattern)
            );
        };
    }
}
