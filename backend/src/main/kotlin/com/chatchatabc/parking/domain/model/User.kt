package com.chatchatabc.parking.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date

@Data
@Entity
@Table(name = "users")
open class User : UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @Column(unique = true)
    open lateinit var email: String

    @Column(unique = true)
    private lateinit var username: String

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private lateinit var password: String

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
    open var verifiedAt: Date? = null

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableList<Role> = mutableListOf()

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    open var parkingLots: MutableList<ParkingLot> = mutableListOf()

    @OneToMany(mappedBy = "user")
    open var vehicles: MutableList<Vehicle> = mutableListOf()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles.stream().map { role -> role as GrantedAuthority }.toList().toMutableList()
    }

    override fun getPassword(): String {
        return this.password
    }

    open fun setPassword(newPassword: String) {
        this.password = newPassword
    }

    override fun getUsername(): String {
        return this.username
    }

    open fun setUsername(newUsername: String) {
        this.username = newUsername
    }

    override fun isAccountNonExpired(): Boolean {
        // TODO: Implement Logic
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        // TODO: Implement Logic
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        // TODO: Implement Logic
        return true
    }

    override fun isEnabled(): Boolean {
        // TODO: Implement Logic
        return true
    }
}