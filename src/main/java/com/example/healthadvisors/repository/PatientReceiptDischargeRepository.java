package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.PatientReceiptDischarge;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PatientReceiptDischargeRepository extends JpaRepository<PatientReceiptDischarge,Integer> {


    List<PatientReceiptDischarge> findPatientReceiptDischargesByDoctor_Id(int doctorId);

}
