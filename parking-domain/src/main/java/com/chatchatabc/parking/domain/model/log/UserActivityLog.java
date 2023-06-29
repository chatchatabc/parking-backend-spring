package com.chatchatabc.parking.domain.model.log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_activity_log")
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

    public UserActivityLog() {
    }

    public UserActivityLog(String id, Long performedBy, String name, String targetId, String eventType, String columnName, String dataBefore, String dataAfter, LocalDateTime createdAt) {
        this.id = id;
        this.performedBy = performedBy;
        this.name = name;
        this.targetId = targetId;
        this.eventType = eventType;
        this.columnName = columnName;
        this.dataBefore = dataBefore;
        this.dataAfter = dataAfter;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataBefore() {
        return dataBefore;
    }

    public void setDataBefore(String dataBefore) {
        this.dataBefore = dataBefore;
    }

    public String getDataAfter() {
        return dataAfter;
    }

    public void setDataAfter(String dataAfter) {
        this.dataAfter = dataAfter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
