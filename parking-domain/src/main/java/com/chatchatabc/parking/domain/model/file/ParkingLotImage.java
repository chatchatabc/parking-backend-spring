package com.chatchatabc.parking.domain.model.file;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "parking_lot_images")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ParkingLotImage extends CloudFileEntity {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @Column
    private int fileOrder;
}
