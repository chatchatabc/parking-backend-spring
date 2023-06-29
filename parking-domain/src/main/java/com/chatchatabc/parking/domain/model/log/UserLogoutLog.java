package com.chatchatabc.parking.domain.model.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_logout_log")
public class UserLogoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "user_id")
    private Long user;

    /**
     * 0: KMM Mobile
     * 1: Admin
     */
    @Column
    private Integer type = 0;

    @Column
    private String ipAddress;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public UserLogoutLog() {
    }

    public UserLogoutLog(Long id, Long user, Integer type, String ipAddress, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
