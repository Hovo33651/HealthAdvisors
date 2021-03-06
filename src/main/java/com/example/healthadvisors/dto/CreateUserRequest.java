package com.example.healthadvisors.dto;

import com.example.healthadvisors.entity.UserType;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.validation.Constraint;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAttribute;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @NotBlank(message = "You forgot to create a password")
    @Size(min = 6, message = "Please input valid password. It should be minimum 6 characters")
    private String password;

    @NotBlank(message = "You forgot to fill the phone number field")
    private String phone;

}

