package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.entity.UserType;
import com.example.healthadvisors.repository.UserRepository;
import com.example.healthadvisors.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final FileUploadUtils fileUploadUtils;

    @Value("${health.advisors.doctor.pictures.upload.path}")
    String doctorPicturePath;
    @Value("${health.advisors.patient.pictures.upload.path}")
    String patientPicPath;

    public User saveUserAsPatient(User user, MultipartFile[] uploadedFiles) throws IOException {
        user.setType(UserType.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = fileUploadUtils.uploadImage(uploadedFiles,patientPicPath);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }

    public User saveUserAsDoctor(User user, MultipartFile[] uploadedFiles) throws IOException {
        user.setType(UserType.DOCTOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = fileUploadUtils.uploadImage(uploadedFiles,doctorPicturePath);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }


}
