package com.chatchatabc.parking.impl.infra.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.chatchatabc.parking.domain.model.file.CloudFile;
import com.chatchatabc.parking.domain.repository.file.CloudFileRepository;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileStorageOSSImpl implements FileStorageService, HealthIndicator {
    @Value("${aliyun.oss.bucket-name:davao-parking}")
    private String bucketName;

    private final OSS ossClient;
    private final CloudFileRepository cloudFileRepository;

    public FileStorageOSSImpl(OSS ossClient, CloudFileRepository cloudFileRepository) {
        this.ossClient = ossClient;
        this.cloudFileRepository = cloudFileRepository;
    }

    /**
     * Upload file to cloud storage
     *
     * @param uploadedById the user who uploaded the file
     * @param namespace    the file namespace
     * @param inputStream  inputStream to upload
     * @param filename     file name
     * @param filesize     file size
     * @param mimetype     file mime type
     * @return cloud file
     */
    @Override
    public CloudFile uploadFile(Long uploadedById, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) {
        // Generate UUID name to be used also as key for cloud storage and append the file extension
        String key = Generators.timeBasedEpochGenerator().generate() + "." + getFileExtension(filename);
        // Create cloud file
        CloudFile cloudFile = new CloudFile();
        cloudFile.setKey(key);
        cloudFile.setName(filename);
        cloudFile.setSize(filesize);
        cloudFile.setMimeType(mimetype);
        cloudFile.setUploadedBy(uploadedById);

        // Upload file to storage service
        ossClient.putObject(bucketName, key, inputStream);
        return cloudFileRepository.save(cloudFile);
    }

    /**
     * Download file from cloud storage
     *
     * @param key file name
     * @return file input stream
     */
    @Override
    public InputStream downloadFile(String key) {
        return ossClient.getObject(bucketName, key).getObjectContent();
    }

    /**
     * Delete file from cloud storage
     *
     * @param key file name
     */
    @Override
    public void deleteFile(String key) {
        ossClient.deleteObject(bucketName, key);
    }

    /**
     * Get the file extension name of a file
     *
     * @param filename the filename
     * @return the file extension name
     */
    public String getFileExtension(String filename) {
        if (filename != null) {
            int lastIndexOfDot = filename.lastIndexOf('.');
            if (lastIndexOfDot > 0 && lastIndexOfDot < filename.length() - 1) {
                return filename.substring(lastIndexOfDot + 1);
            }
        }
        return "";
    }

    @Override
    public Health health() {
        try {
            final OSSObject ossObject = ossClient.getObject(bucketName, "health.txt");
            if (ossObject == null) {
                return Health.down().build();
            } else {
                return Health.up().build();
            }
        } catch (Exception e) {
            return Health.down(e).build();
        }
    }
}
