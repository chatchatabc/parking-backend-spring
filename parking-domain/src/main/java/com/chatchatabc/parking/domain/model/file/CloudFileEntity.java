package com.chatchatabc.parking.domain.model.file;

import com.chatchatabc.parking.domain.model.FlagEntity;
import com.chatchatabc.parking.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class CloudFileEntity extends FlagEntity {
    public static int DELETED = 0;

    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column
    private String filename;

    @Column
    private Long filesize;

    @Column
    private String mimetype;

    @Column
    private String url;

    @JsonIgnore
    public boolean isDeleted() {
        return this.getBitValue(DELETED);
    }

    public void setDeleted(boolean value) {
        this.setBitValue(DELETED, value);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;
}
