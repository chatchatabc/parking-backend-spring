package com.chatchatabc.parking.infra.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {

    /**
     * Upload file to cloud storage
     *
     * @param key           file name
     * @param multipartFile multipartFile to upload
     * @return file url
     */
    String uploadFile(String key, MultipartFile multipartFile) throws IOException;

    /**
     * Download file from cloud storage
     *
     * @param key file name
     * @return file input stream
     */
    InputStream downloadFile(String key);

    /**
     * Delete file from cloud storage
     *
     * @param key file name
     */
    void deleteFile(String key);
}
