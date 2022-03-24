package com.example.healthadvisors.service;


import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.repository.MedReportRepository;
import com.example.healthadvisors.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final MedReportRepository medReportRepository;


    public Patient save(Patient patient) {
        patientRepository.save(patient);
        return patient;
    }

    public List<Patient> findPatientsByMedReports(List<MedReport> medReports) {
        return patientRepository.findPatientsByMedReports(medReports);
    }

    public void deletePatientByID(int id) {
        patientRepository.deleteById(id);
    }

    public Patient findPatientById(int patId) {
        return patientRepository.getById(patId);
    }
}
