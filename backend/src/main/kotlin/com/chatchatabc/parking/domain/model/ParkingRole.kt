package com.chatchatabc.parking.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data

enum class ParkingRoleNames {
    ROLE_OWNER,
    ROLE_MANAGER,
    ROLE_GUARD
}

@Data
@Entity
@Table(name = "parking_roles")
open class ParkingRole(name: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open var name: String = name
}