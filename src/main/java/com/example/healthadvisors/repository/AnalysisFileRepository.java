package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.AnalysisFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisFileRepository extends JpaRepository<AnalysisFile, Integer> {

    List<AnalysisFile> findAnalysisFilesByMedReport_Patient_User_Id(int userId);

}
