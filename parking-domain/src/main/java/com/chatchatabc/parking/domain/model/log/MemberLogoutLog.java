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
@Table(name = "member_logout_log")
@AllArgsConstructor
@NoArgsConstructor
public class MemberLogoutLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @Column(name = "member_id")
    private Long member;

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
}
