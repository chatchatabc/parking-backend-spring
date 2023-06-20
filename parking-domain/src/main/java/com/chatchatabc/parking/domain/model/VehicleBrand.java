package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle_brand")
@AllArgsConstructor
@NoArgsConstructor
public class VehicleBrand {
    public static class VehicleBrandStatus {
        public static final int ACTIVE = 1;
        public static final int DRAFT = 0;
        public static final int INACTIVE = -1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String brandUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column
    private Integer status = VehicleBrandStatus.DRAFT;

    @Column
    private Long createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
