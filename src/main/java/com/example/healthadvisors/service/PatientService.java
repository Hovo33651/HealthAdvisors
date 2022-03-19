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
        return patientRepository.save(patient);
    }

    public void deletePatientByID(int id) {
        patientRepository.deleteById(id);
    }

    public Patient findPatientById(int patId) {
        return patientRepository.getById(patId);
    }
}
