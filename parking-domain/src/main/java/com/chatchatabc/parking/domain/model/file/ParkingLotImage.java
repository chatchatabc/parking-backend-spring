package com.chatchatabc.parking.domain.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "parking_lot_image")
@AllArgsConstructor
@NoArgsConstructor
public class ParkingLotImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "cloud_file_id")
    private CloudFile cloudFile;

    @JsonIgnore
    @Column(name = "parking_lot_id")
    private Long parkingLot;

    @Column
    private int fileOrder = 0;
}
