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
@Table(name = "route")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Route extends FlagEntity {
    public static class RouteStatus {
        public static final int INACTIVE = -1;
        public static final int DRAFT = 0;
        public static final int ACTIVE = 1;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String routeUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Integer status = RouteStatus.DRAFT;

    @Column(columnDefinition = "TEXT")
    private String points;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
