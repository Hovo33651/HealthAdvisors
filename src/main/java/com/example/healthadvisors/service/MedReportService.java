package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.repository.MedReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedReportService {

    private final MedReportRepository medReportRepository;

    public void save(MedReport report){
        medReportRepository.save(report);
    }


    public Page<Patient> findPatientsByDoctorId(int doctorId, Pageable pageable) {
        List<MedReport> medReportsByDoctor_id = medReportRepository.findMedReportsByDoctor_Id(doctorId,pageable);
        List<Patient> patients = new ArrayList<>();
        for (MedReport medReport : medReportsByDoctor_id) {
            patients.add(medReport.getPatient());
        }
        return (Page<Patient>) patients;
    }

}
