package com.chatchatabc.parking.api.impl.application.service

import com.aliyun.oss.OSS
import com.chatchatabc.parking.api.application.service.FileStorageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream

@Service
class FileStorageServiceOSSImpl(
    @Value("\${aliyun.oss.bucket-name}")
    private val bucketName: String,

    private val ossClient: OSS
) : FileStorageService {

    /**
     * Upload a file to the cloud storage
     */
    override fun uploadFile(key: String, file: File) {
        ossClient.putObject(bucketName, key, file)
    }

    /**
     * Download a file from the cloud storage
     */
    override fun downloadFile(key: String): InputStream {
        val ossObject = ossClient.getObject(bucketName, key)
        return ossObject.objectContent
    }

    /**
     * Delete a file from the cloud storage
     */
    override fun deleteFile(key: String) {
        ossClient.deleteObject(bucketName, key)
    }
}