package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.AnalysisFile;
import com.example.healthadvisors.repository.AnalysisFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisFileService {

    private final AnalysisFileRepository analysisFileRepository;


    public List<AnalysisFile> findAnalysisFilesByUserId(int id) {
        return analysisFileRepository.findAnalysisFilesByMedReport_Patient_User_Id(id);
    }
}
