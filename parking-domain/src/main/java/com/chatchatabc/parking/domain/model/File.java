package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class File extends FlagEntity {
    public static int DELETED = 0;

    @Id
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column
    private String parentId;

    @Column
    private String mimetype;

    @Column
    private int fileOrder;

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
