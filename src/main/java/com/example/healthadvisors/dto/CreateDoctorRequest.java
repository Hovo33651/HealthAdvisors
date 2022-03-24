package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequest {

    private int specializationId;
    private int userId;
}
