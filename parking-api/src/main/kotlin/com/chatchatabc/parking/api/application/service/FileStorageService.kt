package com.chatchatabc.parking.api.application.service

import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream

@Service
interface FileStorageService {
    /**
     * Upload a file to the cloud storage
     */
    fun uploadFile(key: String, file: File)

    /**
     * Download a file from the cloud storage
     */
    fun downloadFile(key: String): InputStream

    /**
     * Delete a file from the cloud storage
     */
    fun deleteFile(key: String)
}