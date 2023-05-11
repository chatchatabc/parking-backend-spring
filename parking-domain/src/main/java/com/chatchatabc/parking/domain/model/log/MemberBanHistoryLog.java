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
@Table(name = "member_ban_history_log")
@AllArgsConstructor
@NoArgsConstructor
public class MemberBanHistoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "banned_by", referencedColumnName = "id")
    private Member bannedBy;

    @Column
    private LocalDateTime until;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String reason;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String unbanReason;

    @ManyToOne
    @JoinColumn(name = "unbanned_by", referencedColumnName = "id")
    private Member unbannedBy;

    @Column
    private Integer status = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
