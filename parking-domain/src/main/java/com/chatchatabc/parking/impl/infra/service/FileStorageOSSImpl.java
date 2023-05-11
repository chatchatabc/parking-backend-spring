package com.chatchatabc.parking.impl.infra.service;

import com.aliyun.oss.OSS;
import com.chatchatabc.parking.infra.service.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileStorageOSSImpl implements FileStorageService {
    @Value("${aliyun.oss.bucket-name:davao-parking}")
    private String bucketName;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Autowired
    private OSS ossClient;

    private final Logger log = LoggerFactory.getLogger(FileStorageOSSImpl.class);

    /**
     * Upload file to cloud storage
     *
     * @param key         file name
     * @param inputStream inputStream  to upload
     * @return file url
     */
    @Override
    public String uploadFile(String key, InputStream inputStream) throws IOException {
        // Upload file to storage service
        ossClient.putObject(bucketName, key, inputStream);
        return "https://" + bucketName + "." + endpoint.substring(endpoint.indexOf("://") + 3) + "/" + key;
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
}
