package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.entity.UserType;
import com.example.healthadvisors.repository.UserRepository;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadDownLoadUtils fileUploadUtils;
    private final ModelMapper modelMapper;

    @Value("${health.advisors.doctor.pictures.upload.path}")
    String doctorPicturePath;
    @Value("${health.advisors.patient.pictures.upload.path}")
    String patientPicPath;

    public User saveUserAsPatient(CreateUserRequest createUserRequest, MultipartFile[] uploadedFiles) throws IOException {
        User user = modelMapper.map(createUserRequest, User.class);
        user.setActive(false);
        user.setToken(UUID.randomUUID().toString());
        user.setTokenCreatedDate(LocalDateTime.now());
        user.setType(UserType.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = fileUploadUtils.uploadImage(uploadedFiles,patientPicPath);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }

    public User saveUserAsDoctor(User user, MultipartFile[] uploadedFiles) throws IOException {
        user.setType(UserType.DOCTOR);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        String picUrl = fileUploadUtils.uploadImage(uploadedFiles,doctorPicturePath);
        user.setPicUrl(picUrl);
        userRepository.save(user);
        return user;
    }


    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }


    public List<User> findAll() {
        return userRepository.findAll();
    }



    public void deleteUserById(int userId) {
        userRepository.deleteById(userId);
    }

    public void activateUser(User userFromDb) {
        userFromDb.setActive(true);
        userFromDb.setToken(null);
        userFromDb.setTokenCreatedDate(null);
        userRepository.save(userFromDb);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }


}

