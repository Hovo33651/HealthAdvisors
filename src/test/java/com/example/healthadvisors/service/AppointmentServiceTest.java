package com.example.healthadvisors.service;

import com.example.healthadvisors.entity.Appointment;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentServiceTest {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @MockBean
    private DoctorService doctorService;
    @MockBean
    private MailService mailService;



    @Test
    void findAppointmentsByDoctorId() {

    }

    @Test
    void findAppointmentByPatientId() {
    }

    @Test
    void findAppointmentByDate() {
    }

    @Test
    void deleteAppointmentById() {
    }

    @Test
    void newAppointment() {
    }
}