package com.chatchatabc.parking.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import org.springframework.security.core.GrantedAuthority

@Data
@Entity
@Table(name = "roles")
open class Role(name: String) : GrantedAuthority {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @JsonIgnore
    @Column(unique = true)
    open var name: String = name

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    open var users: MutableList<User> = mutableListOf()

    /**
     * Get Authority
     */
    override fun getAuthority(): String {
        return this.name
    }
}