package com.example.healthadvisors.service;

import com.example.healthadvisors.repository.AnalysisFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalysisFileService {

    private final AnalysisFileRepository analysisFileRepository;

}
