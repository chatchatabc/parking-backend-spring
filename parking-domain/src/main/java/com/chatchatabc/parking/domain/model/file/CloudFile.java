package com.chatchatabc.parking.domain.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "cloud_file")
@AllArgsConstructor
@NoArgsConstructor
public class CloudFile {
    public enum Status {
        DELETED(-1),
        ACTIVE(0);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bucket")
    private String bucket;

    @Column(unique = true)
    private String key;

    @JsonIgnore
    @Column(name = "uploaded_by")
    private Long uploadedBy;

    @Column
    private String name;

    @Column
    private Long size;

    @Column(name = "mime_type")
    private String mimeType;

    @Column
    private String tags;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.ACTIVE;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
