package com.chatchatabc.parking.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "parking_roles")
@AllArgsConstructor
@NoArgsConstructor
public class ParkingRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    /**
     * Constructor
     *
     * @param name name of the role
     */
    public ParkingRole(String name) {
        this.name = name;
    }
}
