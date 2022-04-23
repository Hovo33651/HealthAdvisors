package com.example.healthadvisors.dto;

import antlr.build.ANTLR;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateDoctorRequest {

    private String info;
    private String education;
    private int specializationId;

}
