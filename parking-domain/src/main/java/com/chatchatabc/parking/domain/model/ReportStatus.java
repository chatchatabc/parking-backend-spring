package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_status")
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
    private Integer status = Report.ReportStatus.DRAFT;

    @Column
    private String remarks;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public ReportStatus() {
    }

    public ReportStatus(String id, Long report, Long performedBy, Integer status, String remarks, LocalDateTime createdAt) {
        this.id = id;
        this.report = report;
        this.performedBy = performedBy;
        this.status = status;
        this.remarks = remarks;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getReport() {
        return report;
    }

    public void setReport(Long report) {
        this.report = report;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
