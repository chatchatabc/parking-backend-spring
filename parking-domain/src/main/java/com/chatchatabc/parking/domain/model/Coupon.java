package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Coupon extends FlagEntity {
    public static class CouponStatus {
        public static final int ACTIVE = 1;
        public static final int DRAFT = 0;
        public static final int INACTIVE = -1;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String couponUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String code;

    @Column
    private int status = CouponStatus.DRAFT;

    @Column
    private LocalDateTime valid_until;

    @Column
    private Long createdBy;

    @Column
    private long approvedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
