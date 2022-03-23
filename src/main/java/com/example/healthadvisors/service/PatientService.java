package com.example.healthadvisors.service;


import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.repository.MedReportRepository;
import com.example.healthadvisors.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final MedReportRepository medReportRepository;
    
    public Page<Patient> findPatientsByDoctorId(int id, Pageable pageable){
        List<MedReport> reportList = medReportRepository.findAllByDoctorId(id,pageable);
        List<Patient> allPatients = new ArrayList<>();
        for (MedReport medReport : reportList) {
          allPatients.add(medReport.getPatient());
        }
        return (Page<Patient>) allPatients;

    }

    public Patient save(Patient patient) {
         patientRepository.save(patient);
         return patient;
    }

    public void deletePatientByID(int id) {
        patientRepository.deleteById(id);
    }

    public Patient findPatientById(int patId) {
        return patientRepository.getById(patId);
    }



}
