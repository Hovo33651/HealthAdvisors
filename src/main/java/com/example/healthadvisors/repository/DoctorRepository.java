package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.MedReport;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.Doc;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor,Integer> {

    List<Doctor> findDoctorsBySpecialization_Id(int specId);

    List<Doctor> findDoctorsByMedReports(List<MedReport> medReports);

    Doctor findDoctorByUser_Id(int userId);
}
