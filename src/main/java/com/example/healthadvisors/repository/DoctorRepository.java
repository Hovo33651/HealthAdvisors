package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Integer> {

    List<Doctor> findDoctorsBySpecialization_Id(int specId);

}
