package com.chatchatabc.parking.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Data
@Entity
@Table(name = "vehicles")
open class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column
    open lateinit var name: String

    @Column(unique = true)
    open lateinit var plateNumber: String

    @Column
    open var type: Int = 0

    @JsonIgnore
    @ManyToMany(mappedBy = "vehicles")
    open var users: MutableList<User> = mutableListOf()

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    open lateinit var owner: User

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date
}