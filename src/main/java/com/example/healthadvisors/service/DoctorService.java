package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public Doctor save(Doctor doctor) {
        doctorRepository.save(doctor);
        return doctor;
    }

    public void delete(Doctor doctor) {
        doctorRepository.delete(doctor);
    }

    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> findDoctorsBySpecializationId(int specId) {
        return doctorRepository.findDoctorsBySpecialization_Id(specId);
    }

    public Doctor findDoctorById(int docId){
        return doctorRepository.getById(docId);
    }

}
