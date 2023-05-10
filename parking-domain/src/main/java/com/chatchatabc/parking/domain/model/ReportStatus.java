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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "report_id")
    private Report report;

    @ManyToOne
    @JoinColumn(name = "performed_by")
    private Member performedBy;

    @Column
    private int status;

    @Column
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
