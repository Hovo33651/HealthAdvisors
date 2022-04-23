package com.example.healthadvisors.entity;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patient_receipt_discharge")
public class PatientReceiptDischarge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dischargeDate;
    @OneToMany(mappedBy = "patientReceiptDischarge")
    private List<MedReport> medReports;

}
