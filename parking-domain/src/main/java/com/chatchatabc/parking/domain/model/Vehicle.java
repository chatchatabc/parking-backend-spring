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
import java.util.Collection;

@Data
@Entity
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends BaseEntity {
    @Column
    private String name;

    @Column(unique = true)
    private String plateNumber;

    @Column
    private int type;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    private Collection<User> users;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
