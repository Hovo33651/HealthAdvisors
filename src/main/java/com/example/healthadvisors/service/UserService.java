package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.entity.UserType;
import com.example.healthadvisors.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${health.advisors.upload.path}")
    private String imagePath;

    public User savePatient(User user, MultipartFile[] uploadedFiles) throws IOException {
        user.setType(UserType.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = saveUserImages(uploadedFiles);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }

    private String saveUserImages(MultipartFile[] uploadedFiles) throws IOException {
        if (uploadedFiles.length != 0) {
            for (MultipartFile uploadedFile : uploadedFiles) {
                if (Objects.equals(uploadedFile.getOriginalFilename(), "")) {
                    break;
                }
                String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
                File newFile = new File(imagePath + fileName);
                uploadedFile.transferTo(newFile);
                return newFile.getName();
            }
        }
        return null;
    }

    public User saveDoctor(User user, MultipartFile[] uploadedFiles) throws IOException {
        user.setType(UserType.DOCTOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = saveUserImages(uploadedFiles);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }
}
