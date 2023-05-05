package com.chatchatabc.parking.impl.domain.service.file;

import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.model.file.UserImage;
import com.chatchatabc.parking.domain.repository.file.UserImageRepository;
import com.chatchatabc.parking.domain.service.service.CloudFileService;
import com.chatchatabc.parking.domain.service.service.UserImageService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserImageServiceImpl extends CloudFileService implements UserImageService {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserImageRepository userImageRepository;

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy    the user who uploaded the file
     * @param namespace     the namespace of the file
     * @param multipartFile the file to upload
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    @Override
    public UserImage uploadImage(User uploadedBy, String namespace, MultipartFile multipartFile) throws Exception {
        // Generate UUID name to be used also as key for cloud storage and append the file extension
        String uuid = Generators.timeBasedEpochGenerator().generate() + "." + getFileExtension(multipartFile);
        // Modify file data
        UserImage userImage = new UserImage();
        userImage.setId(uuid);
        userImage.setUploadedBy(uploadedBy);
        userImage.setMimetype(multipartFile.getContentType());
        userImage.setFilename(multipartFile.getOriginalFilename());
        userImage.setFilesize(multipartFile.getSize());
        userImage.setUrl(fileStorageService.uploadFile(namespace + "/" + uuid, multipartFile));
        return userImageRepository.save(userImage);
    }

    /**
     * Mark an image as deleted
     *
     * @param id the id of the image
     */
    @Override
    public void deleteImage(String id) {
        UserImage userImage = userImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User image not found"));
        userImage.setDeleted(true);
        userImageRepository.save(userImage);
    }

    /**
     * Restore an image
     *
     * @param id the id of the image
     */
    @Override
    public void restoreImage(String id) {
        UserImage userImage = userImageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User image not found"));
        userImage.setDeleted(false);
        userImageRepository.save(userImage);
    }
}
