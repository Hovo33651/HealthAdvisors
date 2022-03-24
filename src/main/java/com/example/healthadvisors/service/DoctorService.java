package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.MedReport;
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

    public Doctor findDoctorByUserId(int userId){
        return doctorRepository.findDoctorByUser_Id(userId);
    }

    public List<Doctor> findDoctorsByMedReports(List<MedReport> medReports){
        return doctorRepository.findDoctorsByMedReports(medReports);
    }

}
