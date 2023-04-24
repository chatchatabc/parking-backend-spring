package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rates")
@EnableJpaAuditing
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @OneToOne(mappedBy = "rate", fetch = FetchType.LAZY)
    private ParkingLot parkingLot;

    @Column
    private int type;

    @Column
    private int interval;

    @Column
    private int freeHours;

    @Column
    private boolean payForFreeHoursWhenExceeding;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal startingRate;

    @Column(columnDefinition = "DECIMAL(10,2)")
    private BigDecimal rate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
