package com.chatchatabc.service.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*

@Data
@Entity
@Table(name = "users")
open class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open var email: String? = null

    @Column(unique = true)
    open var username: String? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    open var password: String? = null

    @Column(unique = true)
    open lateinit var phone: String

    @Column
    open var firstName: String? = null

    @Column
    open var lastName: String? = null

    @Column
    open var flag: Int = 0

    @CreationTimestamp
    open lateinit var createdAt: Date

    @UpdateTimestamp
    open lateinit var updatedAt: Date

    @Column
    open var emailVerifiedAt: Date? = null

    @Column
    open var phoneVerifiedAt: Date? = null

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableList<Role> = mutableListOf()

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_vehicles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "vehicle_id")]
    )
    open var vehicles: MutableList<Vehicle> = mutableListOf()

//    override fun isAccountNonExpired(): Boolean {
//        // TODO: Implement Logic
//        return true
//    }
//
//    override fun isAccountNonLocked(): Boolean {
//        // TODO: Implement Logic
//        return true
//    }
//
//    override fun isCredentialsNonExpired(): Boolean {
//        // TODO: Implement Logic
//        return true
//    }
//
//    override fun isEnabled(): Boolean {
//        // TODO: Implement Logic
//        return true
//    }
}