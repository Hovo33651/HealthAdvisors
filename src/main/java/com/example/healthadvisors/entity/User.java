package com.example.healthadvisors.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private String picUrl;
    @JoinColumn(name = "type")
    @Enumerated(EnumType.STRING)
    private UserType type;
    @OneToOne(mappedBy = "user")
    private Patient patient;
    @OneToOne(mappedBy = "user")
    private Doctor doctor;

}
