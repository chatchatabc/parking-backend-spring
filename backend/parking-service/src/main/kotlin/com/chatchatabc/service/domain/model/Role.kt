package com.chatchatabc.service.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data

@Data
@Entity
@Table(name = "roles")
open class Role(name: String) {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open var name: String = name

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    open var users: MutableList<User> = mutableListOf()
}