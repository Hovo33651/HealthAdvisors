package com.example.healthadvisors.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
    private Date birthDate;
    private Gender gender;
    private BloodType bloodType;
    private RhFactor rhFactor;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private String picUrl;

}
