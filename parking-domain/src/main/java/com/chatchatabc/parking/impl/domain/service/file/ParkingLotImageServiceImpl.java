package com.chatchatabc.parking.impl.domain.service.file;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.CloudFile;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository;
import com.chatchatabc.parking.domain.service.file.ParkingLotImageService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ParkingLotImageServiceImpl implements ParkingLotImageService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ParkingLotImageRepository parkingLotImageRepository;

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy  the member who uploaded the file
     * @param parkingLot  the parking lot to which the image belongs
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @param filename    the filename
     * @param filesize    the filesize
     * @param mimetype    the mimetype
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    @Override
    public ParkingLotImage uploadImage(Member uploadedBy, ParkingLot parkingLot, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws Exception {
        // Upload file to cloud storage
        CloudFile cloudFile = fileStorageService.uploadFile(uploadedBy, namespace, inputStream, filename, filesize, mimetype);

        // Create new parking lot image
        ParkingLotImage parkingLotImage = new ParkingLotImage();
        parkingLotImage.setParkingLot(parkingLot);
        parkingLotImage.setFileOrder(0);
        parkingLotImage.setCloudFile(cloudFile);

        return parkingLotImageRepository.save(parkingLotImage);
    }

    /**
     * Mark an image as deleted
     *
     * @param id the id of the image
     */
    @Override
    public void deleteImage(String id) {
        ParkingLotImage parkingLotImage = parkingLotImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Parking lot image not found"));
        parkingLotImage.getCloudFile().setStatus(-1);
        parkingLotImageRepository.save(parkingLotImage);
    }

    /**
     * Restore an image
     *
     * @param id the id of the image
     */
    @Override
    public void restoreImage(String id) {
        ParkingLotImage parkingLotImage = parkingLotImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Parking lot image not found"));
        parkingLotImage.getCloudFile().setStatus(0);
        parkingLotImageRepository.save(parkingLotImage);
    }
}
