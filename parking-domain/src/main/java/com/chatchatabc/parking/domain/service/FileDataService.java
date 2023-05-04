package com.chatchatabc.parking.domain.service;

import com.chatchatabc.parking.domain.model.File;
import com.chatchatabc.parking.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileDataService {

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy the user who uploaded the file
     * @param parentId   the parent id of the file
     * @param fileOrder  the order of the file
     * @param file       the file to upload
     * @return the uploaded file data
     */
    File uploadFile(User uploadedBy, String namespace, String parentId, Integer fileOrder, MultipartFile file) throws IOException;

    /**
     * Mark file as deleted
     *
     * @param id the id of the file
     */
    void deleteFile(String id) throws Exception;

    /**
     * Restore a deleted file
     *
     * @param id the id of the file
     */
    void restoreFile(String id);

    /**
     * Get the file extension of a multipart file.
     *
     * @param file the multipart file
     * @return the file extension
     */
    String getFileExtension(MultipartFile file);
}
