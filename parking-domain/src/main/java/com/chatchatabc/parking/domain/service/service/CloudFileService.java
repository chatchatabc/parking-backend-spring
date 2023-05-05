package com.chatchatabc.parking.domain.service.service;

import org.springframework.web.multipart.MultipartFile;

public class CloudFileService {
    /**
     * Get the file extension name of a file
     *
     * @param file the file
     * @return the file extension name
     */
    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            int lastIndexOfDot = originalFilename.lastIndexOf('.');
            if (lastIndexOfDot > 0 && lastIndexOfDot < originalFilename.length() - 1) {
                return originalFilename.substring(lastIndexOfDot + 1);
            }
        }
        return "";
    }

}
