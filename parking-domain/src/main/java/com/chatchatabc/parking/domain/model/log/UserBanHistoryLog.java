package com.chatchatabc.parking.domain.model.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_ban_history_log")
@AllArgsConstructor
@NoArgsConstructor
public class UserBanHistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @Column(name = "user_id")
    private Long user;

    @JsonIgnore
    @Column(name = "banned_by")
    private Long bannedBy;

    @Column
    private LocalDateTime until;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String unbanReason;

    @JsonIgnore
    @Column(name = "unbanned_by")
    private Long unbannedBy;

    @Column
    private Integer status = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
