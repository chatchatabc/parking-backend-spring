package com.chatchatabc.parking.infra.service;

import com.chatchatabc.parking.domain.model.file.CloudFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {

    /**
     * Upload file to cloud storage
     *
     * @param uploadedById the user who uploaded the file
     * @param namespace    the namespace of the file
     * @param inputStream  inputStream to upload
     * @param filename     file name
     * @param filesize     file size
     * @param mimetype     file mime type
     * @return cloud file
     */
    CloudFile uploadFile(Long uploadedById, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws IOException;

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
