package com.chatchatabc.parking.domain.model.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


@Entity
@Table(name = "parking_lot_image")
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

    public ParkingLotImage() {
    }

    public ParkingLotImage(String id, CloudFile cloudFile, Long parkingLot, int fileOrder) {
        this.id = id;
        this.cloudFile = cloudFile;
        this.parkingLot = parkingLot;
        this.fileOrder = fileOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CloudFile getCloudFile() {
        return cloudFile;
    }

    public void setCloudFile(CloudFile cloudFile) {
        this.cloudFile = cloudFile;
    }

    public Long getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(Long parkingLot) {
        this.parkingLot = parkingLot;
    }

    public int getFileOrder() {
        return fileOrder;
    }

    public void setFileOrder(int fileOrder) {
        this.fileOrder = fileOrder;
    }
}
