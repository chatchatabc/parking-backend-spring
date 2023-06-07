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
@Table(name = "user_activity_log")
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @Column(name = "performed_by")
    private Long performedBy;

    @Column(nullable = false)
    private String name;

    @Column
    private String targetId;

    @Column
    private String eventType;

    @Column
    private String columnName;

    @Column(columnDefinition = "TEXT")
    private String dataBefore;

    @Column(columnDefinition = "TEXT")
    private String dataAfter;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
