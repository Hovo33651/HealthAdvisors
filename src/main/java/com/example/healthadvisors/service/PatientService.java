package com.example.healthadvisors.service;


import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.*;
import com.example.healthadvisors.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

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




    public void deletePatientByUserId(int id) {
        patientRepository.deleteByUserId(id);
    }


    public Patient findPatientById(int patientId){
        return patientRepository.getById(patientId);
    }



    @Transactional
    public Patient registerPatient(CreateUserRequest createUserRequest,
                                CreatePatientRequest createPatientRequest,
                                CreateAddressRequest createAddressRequest,
                                MultipartFile[] uploadedFiles,
                                Locale locale) throws MessagingException, IOException {

        User newUser = userService.saveUserAsPatient(createUserRequest, uploadedFiles);

        Address newAddress = addressService.save(createAddressRequest);

        Patient patient = modelMapper.map(createPatientRequest, Patient.class);

        patient.setUser(newUser);
        patient.setAddress(newAddress);
        patientRepository.save(patient);

        mailService.send(locale, newUser);
        return patient;

    }

}
