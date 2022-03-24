package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.MedReport;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedReportRepository extends JpaRepository<MedReport, Integer> {

    List<MedReport> findAllByDoctor_User_Id(int userId);


    List<MedReport> findAllByPatient_User_Id(int userId);




}
