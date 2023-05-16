package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report_status")
@AllArgsConstructor
@NoArgsConstructor
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @JsonIgnore
    @Column(name = "report_id")
    private Long report;

    @JsonIgnore
    @Column(name = "performed_by")
    private Long performedBy;

    @Column
    private int status;

    @Column
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
