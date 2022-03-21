package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.entity.UserType;
import com.example.healthadvisors.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user){
        user.setType(UserType.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
}
