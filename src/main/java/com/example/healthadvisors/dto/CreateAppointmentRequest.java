package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointmentRequest {
    private int doctorId;
    private int patientId;
    @DateTimeFormat( pattern = "yyyy-MM-dd")
    private Date appointmentDate;
}
