package com.chatchatabc.parking.domain.model.file;

import com.chatchatabc.parking.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
public class CloudFileEntity {
    @JsonIgnore
    @Id
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private Member uploadedBy;

    @Column
    private String filename;

    @Column
    private Long filesize;

    @Column
    private String mimetype;

    @JsonIgnore
    @Column
    private String url;

    @Column
    private Integer status = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
