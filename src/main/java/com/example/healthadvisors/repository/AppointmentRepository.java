package com.example.healthadvisors.repository;

import com.example.healthadvisors.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {

    List<Appointment> findAppointmentsByAppointmentDate(Date date);

    List<Appointment> findAppointmentByActiveIsTrue();

    List<Appointment> findAllByPatientId( int id);

    List<Appointment> findAppointmentByActiveIsTrueAndDoctor_Id(int doctorId);

    List<Appointment>findAppointmentByActiveIsTrueAndPatientId(int patientId);

}
