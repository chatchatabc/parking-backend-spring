package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.model.User;
import com.chatchatabc.parking.domain.repository.RoleRepository;
import com.chatchatabc.parking.domain.repository.UserRepository;
import com.chatchatabc.parking.domain.service.UserService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.chatchatabc.parking.infra.service.KVService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KVService kvService;
    private final FileStorageService fileStorageService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, KVService kvService, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.kvService = kvService;
        this.fileStorageService = fileStorageService;
    }

    private final Random random = new Random();

    /**
     * Soft register a new user if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    @Override
    public void softRegisterUser(String phone, String username) throws Exception {
        Optional<User> queriedUser = userRepository.findByPhone(phone);
        if (queriedUser.isEmpty()) {
            User createdUser = new User();
            createdUser.setPhone(phone);
            createdUser.setStatus(0);
            if (username != null) {
                createdUser.setUsername(username);
            }
            userRepository.save(createdUser);
        } else {
            // Check if username is correct for existing user
            if (username != null) {
                if (!Objects.equals(queriedUser.get().getUsername(), username)) {
                    throw new Exception("Username is incorrect");
                }
            }
        }
    }

    /**
     * Verify if OTP and phone number is valid
     *
     * @param phone    the phone number
     * @param roleName the role name
     * @return the user
     */
    @Override
    public User verifyOTPAndAddRole(String phone, String otp, Role.RoleNames roleName) throws Exception {
        Optional<User> queriedUser = userRepository.findByPhone(phone);
        if (queriedUser.isEmpty()) {
            throw new Exception("User not found");
        }
        Optional<Role> role = roleRepository.findByName(roleName.name());
        if (role.isEmpty()) {
            throw new Exception("Role not found");
        }

        // Check if OTP is valid
        String savedOTP = kvService.get("otp_" + phone);
        if (savedOTP == null) {
            throw new Exception("OTP is expired");
        }
        // TODO: Remove "000000" override in the future
        if (!savedOTP.equals(otp) && !Objects.equals(otp, "000000")) {
            throw new Exception("OTP is incorrect");
        }

        // Delete OTP from KV
        kvService.delete("otp_" + phone);

        // Update phone verified date only if null, no need to reset again
        if (queriedUser.get().getPhoneVerifiedAt() == null) {
            // Set Phone Verified At to current local date time
            queriedUser.get().setPhoneVerifiedAt(LocalDateTime.now());
        }
        // Add role to user if not exists
        if (queriedUser.get().getRoles().stream().noneMatch(r -> Objects.equals(r.getId(), role.get().getId()))) {
            queriedUser.get().getRoles().add(role.get());
        }
        return userRepository.save(queriedUser.get());
    }

    /**
     * Update user
     *
     * @param updatedUser the updated user
     */
    @Override
    public void saveUser(User updatedUser) {
        userRepository.save(updatedUser);
    }

    /**
     * Generate OTP and save to KV
     *
     * @return OTP
     */
    @Override
    public String generateOTPAndSaveToKV(String phone, Long duration) {
        int randomNumber = random.nextInt(1000000);
        String otp = String.format(Locale.ENGLISH, "%06d", randomNumber);
        kvService.set("otp_" + phone, otp, duration);
        return otp;
    }

    /**
     * Upload a file to the storage service.
     *
     * @param uploadedBy  the user who uploaded the file
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @param filename    the filename
     * @param filesize    the filesize
     * @param mimetype    the mimetype
     * @throws Exception if an error occurs
     */
    @Override
    public void uploadImage(User uploadedBy, String namespace, InputStream inputStream, String filename, Long filesize, String mimetype) throws Exception {
        // Update user avatar field
        uploadedBy.setAvatar(fileStorageService.uploadFile(uploadedBy.getId(), namespace, inputStream, filename, filesize, mimetype));
        userRepository.save(uploadedBy);
    }

    /**
     * Login for Security
     *
     * @param username the username
     * @return the user details
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                user.get().isEnabled(),
                user.get().isAccountNonExpired(),
                user.get().isCredentialsNonExpired(),
                user.get().isAccountNonLocked(),
                user.get().getAuthorities()
        );
    }
}
