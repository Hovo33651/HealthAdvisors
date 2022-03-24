package com.example.healthadvisors.dto;

import com.example.healthadvisors.entity.BloodType;
import com.example.healthadvisors.entity.RhFactor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotBlank(message = "You forgot to fill the birth date field")
    private Date birthDate;
    @Enumerated(EnumType.STRING)
    private String gender;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    @Enumerated(EnumType.STRING)
    private RhFactor rhFactor;

}
