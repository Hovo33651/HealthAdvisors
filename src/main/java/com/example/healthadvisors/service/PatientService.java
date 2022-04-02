package com.example.healthadvisors.service;


import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;


    public Patient save(Patient patient) {
        patientRepository.save(patient);
        return patient;
    }


    public void deletePatientByUserId(int id) {
        patientRepository.deleteByUserId(id);
    }


    public Patient findPatientById(int patientId){
        return patientRepository.findById(patientId).orElse(null);
    }
}
