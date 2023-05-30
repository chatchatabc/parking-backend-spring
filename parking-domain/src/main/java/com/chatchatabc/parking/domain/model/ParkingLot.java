package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "parking_lot")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParkingLot extends FlagEntity {
    /**
     * -1: Deleted
     * 0: Draft
     * 1: Pending
     * 2: Rejected
     * 3: Verified
     */
    public enum Status {
        DELETED(-1),
        DRAFT(0),
        PENDING(1),
        REJECTED(2),
        VERIFIED(3);

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

    @Column(unique = true)
    private String parkingLotUuid = UUID.randomUUID().toString();

    @JsonIgnore
    @Column(name = "owner_id", unique = true)
    private Long owner;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_id")
    private Rate rate;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String description;

    @Column
    private Integer capacity;

    @Column
    private Integer availableSlots;

    @Column
    private LocalDateTime businessHoursStart;

    @Column
    private LocalDateTime businessHoursEnd;

    @Column
    private Integer openDaysFlag = 0;

    /**
     * -1: Deleted
     * 0: Draft
     * 1: Pending
     * 2: Rejected
     * 3: Verified
     */
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Status status = Status.DRAFT;

    @Column
    private LocalDateTime verifiedAt;

    @JsonIgnore
    @Column(name = "verified_by")
    private Long verifiedBy;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason = null;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @JsonIgnore
//    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
//    private List<ParkingLotImage> images;
//
//    public List<String> getImagesOrderedByFileOrder() {
//        // Check if there is images, return empty list if there is none
//        if (this.images == null || this.images.isEmpty()) {
//            return List.of();
//        }
//        return this.images.stream()
//                .filter(image -> image.getCloudFile().getStatus() > -1)
//                .sorted(Comparator.comparingInt(ParkingLotImage::getFileOrder))
//                .map(ParkingLotImage::getId)
//                .collect(Collectors.toList());
//    }
}
