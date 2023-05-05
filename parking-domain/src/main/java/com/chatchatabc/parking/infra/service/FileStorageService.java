package com.chatchatabc.parking.infra.service;

import java.io.File;
import java.io.InputStream;

public interface FileStorageService {

    /**
     * Upload file to cloud storage
     *
     * @param key  file name
     * @param file file to upload
     * @return file url
     */
    String uploadFile(String key, File file);

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
