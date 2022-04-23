package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.PatientReceiptDischarge;
import com.example.healthadvisors.repository.MedReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedReportService {

    private final MedReportRepository medReportRepository;
    private final PatientReceiptDischargeService patientReceiptDischargeService;

    public void save(MedReport report){
        medReportRepository.save(report);
    }


    public Page<Patient> findPatientsByDoctorId(int doctorId, Pageable pageable) {
        List<PatientReceiptDischarge> patientReceiptDischarges = patientReceiptDischargeService.findPatientReceiptDischargesByDoctorId(doctorId);
        List<Patient> patients = new ArrayList<>();
        for (PatientReceiptDischarge patientReceiptDischarge : patientReceiptDischarges) {
            patients.add(patientReceiptDischarge.getPatient());
        }
        return new PageImpl<>(patients, pageable, pageable.getOffset());
    }

}
