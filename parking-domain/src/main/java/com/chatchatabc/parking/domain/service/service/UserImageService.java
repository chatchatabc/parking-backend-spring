package com.chatchatabc.parking.domain.service.service;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.file.UserImage;
import org.springframework.web.multipart.MultipartFile;

public interface UserImageService {

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy    the user who uploaded the file
     * @param namespace     the namespace of the file
     * @param multipartFile the file to upload
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    UserImage uploadImage(User uploadedBy, String namespace, MultipartFile multipartFile) throws Exception;

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
