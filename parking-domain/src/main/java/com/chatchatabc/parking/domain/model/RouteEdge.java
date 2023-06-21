package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "route_edge")
@AllArgsConstructor
@NoArgsConstructor
public class RouteEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String routeUuid;

    @Column
    private Long nodeFrom;

    @Column
    private Long nodeTo;

    @Column
    private Double distance;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
