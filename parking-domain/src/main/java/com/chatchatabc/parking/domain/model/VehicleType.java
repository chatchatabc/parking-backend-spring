package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "vehicle_type")
@AllArgsConstructor
@NoArgsConstructor
public class VehicleType {
    public static class VehicleTypeStatus {
        public static final int ACTIVE = 1;
        public static final int DRAFT = 0;
        public static final int INACTIVE = -1;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String typeUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column
    private Integer status = VehicleTypeStatus.DRAFT;

    @JsonIgnore
    @Column
    private Long createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
