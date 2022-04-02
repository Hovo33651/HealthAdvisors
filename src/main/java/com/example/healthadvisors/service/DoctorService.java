package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public void delete(Doctor doctor) {
        doctorRepository.delete(doctor);
    }

    public Page<Doctor> findAllDoctors(PageRequest pageRequest) {
        return doctorRepository.findAll(pageRequest);
    }

    public List<Doctor> findDoctorsBySpecializationId(int specId) {
        return doctorRepository.findDoctorsBySpecialization_Id(specId);
    }

    public Doctor findDoctorById(int doctorId){
        return doctorRepository.findById(doctorId).orElse(null);
    }


}
