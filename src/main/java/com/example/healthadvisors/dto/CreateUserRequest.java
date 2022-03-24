package com.example.healthadvisors.dto;

import com.example.healthadvisors.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.validation.Constraint;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotBlank(message = "What is your name?")
    @Size(min = 3, max = 10, message = "Check you name")
    private String name;
    @NotBlank(message = "What is your surname?")
    @Size(min = 3, max = 10, message = "Check your surname")
    private String surname;
    @Email
    @NotBlank(message = "What is your email address")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please input valid email address")
    private String email;
    @NotEmpty
    @Size(min = 6, message = "Please input valid password. It should be minimum 6 characters")
    private String password;
    private String phone;
    private String picUrl;
    @JoinColumn(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;
}

