package com.example.healthadvisors.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMedReportRequest {

    private int doctorId;
    private int patientId;
    private String report;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dischargeDate;
}
