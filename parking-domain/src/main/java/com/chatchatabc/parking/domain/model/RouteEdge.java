package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_edge")
public class RouteEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long routeId;

    @Column
    private Long nodeFrom;

    @Column
    private Long nodeTo;

    @Column
    private Double distance;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public RouteEdge() {
    }

    public RouteEdge(Long id, Long routeId, Long nodeFrom, Long nodeTo, Double distance, LocalDateTime createdAt) {
        this.id = id;
        this.routeId = routeId;
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.distance = distance;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getNodeFrom() {
        return nodeFrom;
    }

    public void setNodeFrom(Long nodeFrom) {
        this.nodeFrom = nodeFrom;
    }

    public Long getNodeTo() {
        return nodeTo;
    }

    public void setNodeTo(Long nodeTo) {
        this.nodeTo = nodeTo;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
