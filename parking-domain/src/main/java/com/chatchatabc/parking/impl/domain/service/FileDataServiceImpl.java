package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.File;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.FileDataRepository;
import com.chatchatabc.parking.domain.service.FileDataService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.fasterxml.uuid.Generators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileDataServiceImpl implements FileDataService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private FileDataRepository fileDataRepository;

    private final Logger log = LoggerFactory.getLogger(FileDataServiceImpl.class);

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy the user who uploaded the file
     * @param parentId   the parent id of the file
     * @param fileOrder  the order of the file
     * @param file       the file to upload
     * @return the uploaded file data
     */
    @Override
    public File uploadFile(User uploadedBy, String namespace, String parentId, Integer fileOrder, MultipartFile file) throws IOException {
        // Generate UUID name to be used also as key for cloud storage and append the file extension
        String uuid = Generators.timeBasedEpochGenerator().generate() + "." + getFileExtension(file);
        // Create file data
        File fileData = new File();
        fileData.setId(uuid);
        fileData.setParentId(parentId);
        fileData.setUploadedBy(uploadedBy);
        fileData.setMimetype(file.getContentType());
        fileData.setFileOrder(fileOrder);

        // Upload file to storage service
        // Create temporary file first
        java.io.File tempFile = java.io.File.createTempFile("temp", null);
        // Save multipart file to temporary file
        file.transferTo(tempFile);
        // Upload file to storage service
        fileStorageService.uploadFile(namespace + "/" + uuid, tempFile);
        // Delete temporary file
        boolean isDeleted = tempFile.delete();
        if (!isDeleted) {
            log.warn("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }
        return fileDataRepository.save(fileData);
    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            int lastIndexOfDot = originalFilename.lastIndexOf('.');
            if (lastIndexOfDot > 0 && lastIndexOfDot < originalFilename.length() - 1) {
                return originalFilename.substring(lastIndexOfDot + 1);
            }
        }
        return "";
    }

}
