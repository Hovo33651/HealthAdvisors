package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate,Integer> {

    List<Certificate> findCertificatesByDoctor_Id(int doctorId);
}
