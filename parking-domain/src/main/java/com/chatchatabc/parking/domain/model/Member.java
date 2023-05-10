package com.chatchatabc.parking.domain.model;

import com.chatchatabc.parking.domain.model.log.MemberBanHistoryLog;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Member extends FlagEntity implements UserDetails {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", unique = true)
    private String memberId = UUID.randomUUID().toString();

    @JsonIgnore
    @Column(unique = true)
    private String notificationId = UUID.randomUUID().toString();

    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$", message = "Invalid email format.")
    private String email;

    @Column(unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,15}$", message = "Invalid username format.")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format.")
    private String phone;

    @Column
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name can only contain letters and spaces.")
    private String firstName;

    @Column
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name can only contain letters and spaces.")
    private String lastName;

    @JsonIgnore
    @Column
    private String avatar;

    @Column
    private Integer status = 0;

    @Column
    private LocalDateTime emailVerifiedAt;

    @Column
    private LocalDateTime phoneVerifiedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "member_role",
            joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "member_vehicle",
            joinColumns = @JoinColumn(name = "member_id", referencedColumnName = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id")
    )
    private Collection<Vehicle> vehicles;

    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Collection<MemberBanHistoryLog> memberBanHistoryLogs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Check if member is banned, use MemberBanHistoryLog
     *
     * @return true if member is not banned, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        // Check if memberBanHistoryLogs has an active ban
        if (this.memberBanHistoryLogs != null) {
            for (MemberBanHistoryLog memberBanHistoryLog : this.memberBanHistoryLogs) {
                if (memberBanHistoryLog.getStatus() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status > -1;
    }
}
