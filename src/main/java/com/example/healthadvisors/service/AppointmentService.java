package com.example.healthadvisors.service;


import com.example.healthadvisors.entity.Appointment;
import com.example.healthadvisors.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public void saveAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    List<Appointment> findAppointmentsByDoctorId(int id) {

        return appointmentRepository.findAllByDoctor_Id(id);
    }

    List<Appointment>findAppointmentByPatientId(int id){

        return appointmentRepository.findAllByPatientId(id);
    }
    List<Appointment>findAppointmentByDate(Date date){
        return appointmentRepository.findAppointmentsByAppointmentDate(date);
    }


}
