package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle")
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String vehicleUuid = UUID.randomUUID().toString();

    @Column
    private String name;

    @Column(unique = true)
    private String plateNumber;

    @Column
    private String brandUuid;

    @Column
    private String modelUuid;

    @Column
    private String typeUuid;

    @Column
    private String color;

    @Column
    private String year;

    // TODO:
    // add image for front, back, and side view of the vehicle

    // TODO: Verified at
    // TODO: Verified by

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    private Collection<User> users;

    @JsonIgnore
    @Column(name = "owner_id")
    private Long owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
