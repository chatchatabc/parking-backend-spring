package com.chatchatabc.parking.domain.model.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.chatchatabc.parking.domain.model.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_login_logs")
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String email;

    @Column
    private String phone;

    @Column
    private String ipAddress;

    @Column
    private Boolean success = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
