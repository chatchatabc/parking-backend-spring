package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ParkingLotSpecification {

    public static Specification<ParkingLot> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";

            // Join the owner user entity
            Join<ParkingLot, User> owner = root.join("owner");

            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern),
                    builder.like(builder.lower(root.get("address")), pattern),
                    builder.like(builder.lower(owner.get("username")), pattern),
                    builder.like(builder.lower(owner.get("email")), pattern),
                    builder.like(builder.lower(owner.get("firstName")), pattern),
                    builder.like(builder.lower(owner.get("lastName")), pattern),
                    builder.like(builder.lower(owner.get("phone")), pattern)
            );
        };
    }
}
