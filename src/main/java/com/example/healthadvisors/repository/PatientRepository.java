package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Integer> {

}
