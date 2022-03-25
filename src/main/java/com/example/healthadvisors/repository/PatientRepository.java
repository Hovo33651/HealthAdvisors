package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient,Integer> {

}
