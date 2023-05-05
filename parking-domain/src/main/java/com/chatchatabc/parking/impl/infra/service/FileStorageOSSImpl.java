package com.chatchatabc.parking.impl.infra.service;

import com.aliyun.oss.OSS;
import com.chatchatabc.parking.infra.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class FileStorageOSSImpl implements FileStorageService {
    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Autowired
    private OSS ossClient;

    /**
     * Upload file to cloud storage
     *
     * @param key  file name
     * @param file file to upload
     * @return file url
     */
    @Override
    public String uploadFile(String key, File file) {
        ossClient.putObject(bucketName, key, file);
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
