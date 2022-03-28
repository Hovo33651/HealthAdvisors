package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findUserByEmail(String email);

    Optional<User> findByToken(String token);


}
