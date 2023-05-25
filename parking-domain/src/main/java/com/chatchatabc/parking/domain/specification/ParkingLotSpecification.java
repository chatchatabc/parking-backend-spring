package com.chatchatabc.parking.domain.specification;

import com.chatchatabc.parking.domain.model.ParkingLot;
import org.springframework.data.jpa.domain.Specification;

public class ParkingLotSpecification {

    public static Specification<ParkingLot> withKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";

            // Find a way to join the owner member entity without entity mapping
            // Join the owner member entity
            // Join<ParkingLot, Member> owner = root.join("owner");

            return builder.or(
                    builder.like(builder.lower(root.get("name")), pattern),
                    builder.like(builder.lower(root.get("address")), pattern)
//                    builder.like(builder.lower(owner.get("username")), pattern),
//                    builder.like(builder.lower(owner.get("email")), pattern),
//                    builder.like(builder.lower(owner.get("firstName")), pattern),
//                    builder.like(builder.lower(owner.get("lastName")), pattern),
//                    builder.like(builder.lower(owner.get("phone")), pattern)
            );

            // Subquery to join the owner member entity
//            Subquery<Member> subquery = query.subquery(Member.class);
//            Root<Member> subqueryRoot = subquery.from(Member.class);
//            Join<Member, ParkingLot> subqueryJoin = subqueryRoot.join("parkingLots");
//            subquery.select(subqueryJoin.getParent());
//
//            return builder.or(
//                    builder.like(builder.lower(root.get("name")), pattern),
//                    builder.like(builder.lower(root.get("address")), pattern),
//                    builder.like(builder.lower(subqueryRoot.get("username")), pattern),
//                    builder.like(builder.lower(subqueryRoot.get("email")), pattern),
//                    builder.like(builder.lower(subqueryRoot.get("firstName")), pattern),
//                    builder.like(builder.lower(subqueryRoot.get("lastName")), pattern),
//                    builder.like(builder.lower(subqueryRoot.get("phone")), pattern)
//            );
        };
    }
}
