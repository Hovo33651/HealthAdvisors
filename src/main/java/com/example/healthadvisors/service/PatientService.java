package com.example.healthadvisors.service;


import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.*;
import com.example.healthadvisors.repository.PatientRepository;
import com.example.healthadvisors.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.mail.MailMessage;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final MailService mailService;
    private final AppointmentService appointmentService;
    private final ModelMapper modelMapper;

    private final UserService userService;
    private final AddressService addressService;


    public Patient save(Patient patient) {
        patientRepository.save(patient);
        return patient;
    }


    public void deletePatientByUserId(int id) {
        patientRepository.deleteByUserId(id);
    }


    public Patient findPatientById(int patientId){
        return patientRepository.findById(patientId).orElse(null);
    }

    public void newAppointment(int doctorId, String appointmentDate, CurrentUser currentUser) {
        Doctor doctor = doctorService.findDoctorById(doctorId);
        Patient patient = currentUser.getUser().getPatient();

        Appointment newAppointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDateTime.parse(appointmentDate))
                .build();

        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);

        appointmentService.saveAppointment(newAppointment);

        String subject = "New Appointment";
        String message = "Doctor " +  doctor.getUser().getSurname() + ", you have a new appointment. \n " +
                "Patient: " + patient.getUser().getName() + " " + patient.getUser().getSurname() + "\n" +
                "Date " + newAppointment.getAppointmentDate();

        mailService.sendEmail(doctor.getUser().getEmail(),subject,message);
    }

    public void registerUser(CreateUserRequest createUserRequest, CreatePatientRequest createPatientRequest, CreateAddressRequest createAddressRequest, MultipartFile[] uploadedFiles, ModelMap map, Locale locale) throws MessagingException, IOException {

        User user = modelMapper.map(createUserRequest, User.class);
        user.setActive(false);
        user.setToken(UUID.randomUUID().toString());
        user.setTokenCreatedDate(LocalDateTime.now());
        User newUser = userService.saveUserAsPatient(user, uploadedFiles);

        Address newAddress = addressService.save(modelMapper.map(createAddressRequest, Address.class));

        Patient patient = modelMapper.map(createPatientRequest, Patient.class);
        patient.setUser(newUser);
        patient.setAddress(newAddress);
        save(patient);

        String subject = "WELCOME TO OUR WEBSITE";
        String link = "localhost:8080/user/activate?token="+newUser.getToken();

        mailService.sendHtmlEmail(newUser.getEmail(),subject,newUser,link,"404", locale);

    }
}
