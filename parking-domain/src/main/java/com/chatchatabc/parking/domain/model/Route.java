package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "route")
public class Route extends FlagEntity {
    public static class RouteStatus {
        public static final int INACTIVE = -1;
        public static final int DRAFT = 0;
        public static final int ACTIVE = 1;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String routeUuid = UUID.randomUUID().toString();

    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Invalid slug format.")
    private String slug;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String color;

    @Column
    private Integer status = RouteStatus.DRAFT;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Route() {
    }

    public Route(Long id, String routeUuid, String slug, String name, String description, String color, Integer status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.routeUuid = routeUuid;
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.color = color;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouteUuid() {
        return routeUuid;
    }

    public void setRouteUuid(String routeUuid) {
        this.routeUuid = routeUuid;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
