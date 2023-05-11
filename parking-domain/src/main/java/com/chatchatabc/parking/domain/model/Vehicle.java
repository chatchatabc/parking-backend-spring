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

@Data
@Entity
@Table(name = "vehicle")
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column(unique = true)
    private String plateNumber;

    @Column
    private int type;

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    private Collection<Member> members;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Member owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
