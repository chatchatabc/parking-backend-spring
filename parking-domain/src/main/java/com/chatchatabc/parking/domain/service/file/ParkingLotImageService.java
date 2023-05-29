package com.chatchatabc.parking.domain.service.file;

import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;

import java.io.InputStream;
import java.util.List;

public interface ParkingLotImageService {

    /**
     * Update order of images of a Parking Lot
     *
     * @param images the images
     */
    void updateOrderOfImages(List<ParkingLotImage> images) throws Exception;

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy  the user who uploaded the file
     * @param parkingLot  the parking lot to which the image belongs
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @param filename    the filename
     * @param filesize    the filesize
     * @param mimetype    the mimetype
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    ParkingLotImage uploadImage(User uploadedBy, ParkingLot parkingLot, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws Exception;

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
