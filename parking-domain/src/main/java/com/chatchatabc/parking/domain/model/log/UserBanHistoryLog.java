package com.chatchatabc.parking.domain.model.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_ban_history_log")
public class UserBanHistoryLog {
    public static final int UNBANNED = -1;
    public static final int BANNED = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "user_id")
    private Long user;

    @JsonIgnore
    @Column(name = "banned_by")
    private Long bannedBy;

    @Column
    private LocalDateTime until;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(columnDefinition = "TEXT")
    private String unbanReason;

    @JsonIgnore
    @Column(name = "unbanned_by")
    private Long unbannedBy;

    @Column
    private LocalDateTime unbannedAt;

    @Column
    private Integer status = BANNED;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public UserBanHistoryLog() {
    }

    public UserBanHistoryLog(Long id, Long user, Long bannedBy, LocalDateTime until, String reason, String unbanReason, Long unbannedBy, LocalDateTime unbannedAt, Integer status, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.bannedBy = bannedBy;
        this.until = until;
        this.reason = reason;
        this.unbanReason = unbanReason;
        this.unbannedBy = unbannedBy;
        this.unbannedAt = unbannedAt;
        this.status = status;
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

    public Long getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(Long bannedBy) {
        this.bannedBy = bannedBy;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUnbanReason() {
        return unbanReason;
    }

    public void setUnbanReason(String unbanReason) {
        this.unbanReason = unbanReason;
    }

    public Long getUnbannedBy() {
        return unbannedBy;
    }

    public void setUnbannedBy(Long unbannedBy) {
        this.unbannedBy = unbannedBy;
    }

    public LocalDateTime getUnbannedAt() {
        return unbannedAt;
    }

    public void setUnbannedAt(LocalDateTime unbannedAt) {
        this.unbannedAt = unbannedAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
