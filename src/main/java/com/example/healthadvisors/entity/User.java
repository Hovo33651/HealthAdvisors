package com.example.healthadvisors.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private String token;
    private boolean active;
    private LocalDateTime tokenCreatedDate;
    @OneToMany(mappedBy = "user")
    private List<Testimonial> testimonials;

}
