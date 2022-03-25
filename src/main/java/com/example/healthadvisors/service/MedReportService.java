package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.repository.MedReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedReportService {

    private final MedReportRepository medReportRepository;

    public void save(MedReport report){
        medReportRepository.save(report);
    }

    public List<MedReport> findMedReportByPatientId(int userId){
        return medReportRepository.findAllByPatient_User_Id(userId);
    }

    public List<MedReport> findMedReportsByDoctorUserId(int userId){
        return medReportRepository.findMedReportsByDoctor_User_Id(userId);
    }
}
