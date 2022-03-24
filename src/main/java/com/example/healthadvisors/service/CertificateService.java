package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Certificate;
import com.example.healthadvisors.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public void save(Certificate certificate){
        certificateRepository.save(certificate);
    }

    public List<Certificate> findCertificatesByDoctorId(int doctorId){
      return certificateRepository.findCertificatesByDoctor_Id(doctorId);
    }
}
