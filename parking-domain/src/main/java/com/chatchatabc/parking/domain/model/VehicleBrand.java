package com.chatchatabc.parking.domain.model;

import com.chatchatabc.parking.domain.model.file.CloudFile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle_brand")
public class VehicleBrand {
    public static class VehicleBrandStatus {
        public static final int ACTIVE = 1;
        public static final int DRAFT = 0;
        public static final int INACTIVE = -1;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String brandUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "logo")
    private CloudFile logo;

    @Column
    private Integer status = VehicleBrandStatus.DRAFT;

    @JsonIgnore
    @Column
    private Long createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public VehicleBrand() {
    }

    public VehicleBrand(Long id, String brandUuid, String name, CloudFile logo, Integer status, Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.brandUuid = brandUuid;
        this.name = name;
        this.logo = logo;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandUuid() {
        return brandUuid;
    }

    public void setBrandUuid(String brandUuid) {
        this.brandUuid = brandUuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CloudFile getLogo() {
        return logo;
    }

    public void setLogo(CloudFile logo) {
        this.logo = logo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
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
