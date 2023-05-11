package com.chatchatabc.parking.domain.model.file;

import com.chatchatabc.parking.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cloud_file")
@AllArgsConstructor
@NoArgsConstructor
public class CloudFile {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
