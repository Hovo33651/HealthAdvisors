package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Integer> {

    @Override
    Optional<Patient> findById(Integer integer);
    Patient findByUser_Id(int userId);

    void deleteByUserId(int userId);
}
