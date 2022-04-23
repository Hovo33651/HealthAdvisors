package com.example.healthadvisors.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
@Builder
@Entity
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String info;
    private String education;
    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
    @OneToMany(mappedBy = "doctor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Certificate> certificates;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "doctor")
    private List<PatientReceiptDischarge> patientReceiptDischarges;
    @Transient
    private int rating;



}
