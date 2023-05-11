package com.chatchatabc.parking.impl.domain.service;

import com.chatchatabc.parking.domain.enums.RoleNames;
import com.chatchatabc.parking.domain.model.Member;
import com.chatchatabc.parking.domain.model.Role;
import com.chatchatabc.parking.domain.repository.MemberRepository;
import com.chatchatabc.parking.domain.repository.RoleRepository;
import com.chatchatabc.parking.domain.service.MemberService;
import com.chatchatabc.parking.domain.service.service.CloudFileService;
import com.chatchatabc.parking.infra.service.FileStorageService;
import com.chatchatabc.parking.infra.service.KVService;
import com.fasterxml.uuid.Generators;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberServiceImpl extends CloudFileService implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private KVService kvService;
    @Autowired
    private FileStorageService fileStorageService;

    private final Random random = new Random();

    /**
     * Soft register a new member if not exists
     *
     * @param phone    the phone number
     * @param username the username
     */
    @Override
    public void softRegisterMember(String phone, String username) throws Exception {
        Optional<Member> queriedMember = memberRepository.findByPhone(phone);
        if (queriedMember.isEmpty()) {
            Member createdMember = new Member();
            createdMember.setPhone(phone);
            createdMember.setStatus(0);
            if (username != null) {
                createdMember.setUsername(username);
            }
            memberRepository.save(createdMember);
        } else {
            // Check if username is correct for existing member
            if (username != null) {
                if (!Objects.equals(queriedMember.get().getUsername(), username)) {
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
     * @return the member
     */
    @Override
    public Member verifyOTPAndAddRole(String phone, String otp, RoleNames roleName) throws Exception {
        Optional<Member> queriedMember = memberRepository.findByPhone(phone);
        if (queriedMember.isEmpty()) {
            throw new Exception("Member not found");
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
        if (queriedMember.get().getPhoneVerifiedAt() == null) {
            // Set Phone Verified At to current local date time
            queriedMember.get().setPhoneVerifiedAt(LocalDateTime.now());
        }
        // Add role to member if not exists
        if (queriedMember.get().getRoles().stream().noneMatch(r -> Objects.equals(r.getId(), role.get().getId()))) {
            queriedMember.get().getRoles().add(role.get());
        }
        return memberRepository.save(queriedMember.get());
    }

    /**
     * Update member
     *
     * @param memberId  the user id
     * @param username  the username
     * @param email     the email
     * @param firstName the first name
     * @param lastName  the last name
     * @return the member
     */
    @Override
    public Member updateMember(String memberId, String phone, String username, String email, String firstName, String lastName) throws Exception {
        Optional<Member> queriedMember = memberRepository.findByMemberId(memberId);
        if (queriedMember.isEmpty()) {
            throw new Exception("Member not found");
        }

        // Apply Changes
        if (phone != null) {
            queriedMember.get().setPhone(phone);
        }
        if (username != null) {
            queriedMember.get().setUsername(username);
        }
        if (email != null) {
            queriedMember.get().setEmail(email);
        }
        if (firstName != null) {
            queriedMember.get().setFirstName(firstName);
        }
        if (lastName != null) {
            queriedMember.get().setLastName(lastName);
        }
        return memberRepository.save(queriedMember.get());
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
     * @param uploadedBy  the member who uploaded the file
     * @param namespace   the namespace of the file
     * @param inputStream the file to upload
     * @return the uploaded file data
     * @throws Exception if an error occurs
     */
    @Override
    public Member uploadImage(Member uploadedBy, String namespace, InputStream inputStream) throws Exception {
        // Generate UUID name to be used also as key for cloud storage and append the file extension
        String uuid = Generators.timeBasedEpochGenerator().generate() + "." + getFileExtension(inputStream);
        // Update member avatar field
        uploadedBy.setAvatar(fileStorageService.uploadFile(namespace + "/" + uuid, inputStream));
        return memberRepository.save(uploadedBy);
    }

    /**
     * Login for Security
     *
     * @param username the username
     * @return the member details
     * @throws UsernameNotFoundException if member not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByUsername(username);
        if (member.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                member.get().getUsername(),
                member.get().getPassword(),
                member.get().isEnabled(),
                member.get().isAccountNonExpired(),
                member.get().isCredentialsNonExpired(),
                member.get().isAccountNonLocked(),
                member.get().getAuthorities()
        );
    }
}
