package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.PatientReceiptDischarge;
import com.example.healthadvisors.repository.PatientReceiptDischargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientReceiptDischargeService {

    private final PatientReceiptDischargeRepository patientReceiptDischargeRepository;


    public List<PatientReceiptDischarge> findPatientReceiptDischargesByDoctorId(int doctorId) {
        return patientReceiptDischargeRepository.findPatientReceiptDischargesByDoctor_Id(doctorId);
    }
}
