package com.chatchatabc.parking.domain.model;

import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "parking_lot")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParkingLot extends FlagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "member_id")
    private Member owner;

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

    @Column
    private Integer status = 0;

    @Column
    private LocalDateTime verifiedAt;

    // Verified by member
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private Member verifiedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY)
    private List<ParkingLotImage> images;

    public List<String> getImagesOrderedByFileOrder() {
        // Check if there is images, return empty list if there is none
        if (this.images == null || this.images.isEmpty()) {
            return List.of();
        }
        return this.images.stream()
                .filter(image -> image.getStatus() > -1)
                .sorted(Comparator.comparingInt(ParkingLotImage::getFileOrder))
                .map(ParkingLotImage::getId)
                .collect(Collectors.toList());
    }
}
