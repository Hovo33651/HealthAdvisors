package com.example.healthadvisors.dto;

import com.example.healthadvisors.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private String picUrl;
    @JoinColumn(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;
}

