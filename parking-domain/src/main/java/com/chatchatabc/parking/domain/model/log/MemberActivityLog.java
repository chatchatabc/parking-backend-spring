package com.chatchatabc.parking.domain.model.log;

import com.chatchatabc.parking.domain.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "member_activity_log")
@AllArgsConstructor
@NoArgsConstructor
public class MemberActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member performedBy;

    @Column(nullable = false)
    private String name;

    @Column
    private String targetId;

    @Column
    private String eventType;

    @Column
    private String columnName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String dataBefore;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String dataAfter;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
