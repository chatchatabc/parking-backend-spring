package com.chatchatabc.parking.impl.domain.service.file;

import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.ParkingLot;
import com.chatchatabc.parking.domain.model.file.ParkingLotImage;
import com.chatchatabc.parking.domain.repository.file.ParkingLotImageRepository;
import com.chatchatabc.parking.domain.service.service.CloudFileService;
import com.chatchatabc.parking.domain.service.service.ParkingLotImageService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ParkingLotImageServiceImpl extends CloudFileService implements ParkingLotImageService {
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
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    @Override
    public ParkingLotImage uploadImage(Member uploadedBy, ParkingLot parkingLot, String namespace, InputStream inputStream) throws Exception {
        // Generate UUID name to be used also as key for cloud storage and append the file extension
        String uuid = Generators.timeBasedEpochGenerator().generate() + "." + getFileExtension(inputStream);
        // Modify file data
        ParkingLotImage parkingLotImage = new ParkingLotImage();
        parkingLotImage.setId(uuid);
        parkingLotImage.setUploadedBy(uploadedBy);
        parkingLotImage.setParkingLot(parkingLot);
//        parkingLotImage.setMimetype(multipartFile.getContentType());
//        parkingLotImage.setFilename(multipartFile.getOriginalFilename());
//        parkingLotImage.setFilesize(multipartFile.getSize());
        parkingLotImage.setUrl(fileStorageService.uploadFile(namespace + "/" + uuid, inputStream));
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
        parkingLotImage.setStatus(-1);
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
        parkingLotImage.setStatus(0);
        parkingLotImageRepository.save(parkingLotImage);
    }
}
