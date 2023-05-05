package com.chatchatabc.parking.domain.service.service;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import org.springframework.web.multipart.MultipartFile;

public interface ParkingLotImageService {
    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy the user who uploaded the file
     * @param parkingLot the parking lot to which the image belongs
     * @param namespace  the namespace of the file
     * @param file       the file to upload
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    ParkingLotImage uploadImage(User uploadedBy, ParkingLot parkingLot, String namespace, MultipartFile file) throws Exception;

    /**
     * Mark an image as deleted
     *
     * @param id the id of the image
     */
    void deleteImage(String id);

    /**
     * Restore an image
     *
     * @param id the id of the image
     */
    void restoreImage(String id);
}
