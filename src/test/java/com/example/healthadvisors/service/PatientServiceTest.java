package com.example.healthadvisors.service;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.BloodType;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.RhFactor;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.repository.PatientRepository;
import com.example.healthadvisors.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class PatientServiceTest {

    @Autowired
    private PatientService patientService;
    @MockBean
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private MailService mailService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private PatientRepository patientRepository;

    @Test
    void registerPatient() throws Exception {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .name("poxos")
                .surname("poxosyan")
                .email("poxos@mail.com")
                .password("123456")
                .phone("123")
                .build();
        CreatePatientRequest createPatientRequest = CreatePatientRequest.builder()
                .rhFactor(RhFactor.POSITIVE)
                .bloodType(BloodType.I)
                .gender("MALE")
                .birthDate(new SimpleDateFormat("dd.MM.yyyy").parse("04.04.1994"))
                .build();
        CreateAddressRequest createAddressRequest = CreateAddressRequest.builder()
                .country("Armenia")
                .region("Shirak")
                .city("Gyumri")
                .street("P Sevak")
                .address("18g 22")
                .build();
        MultipartFile[] multipartFiles = new MultipartFile[0];
        Locale locale = new Locale("eng");

        patientService.registerPatient(createUserRequest, createPatientRequest, createAddressRequest, multipartFiles, locale);
        Optional<User> userByEmail = userRepository.findUserByEmail(createUserRequest.getEmail());

        assertTrue(userByEmail.isPresent());


        verify(userService, times(1)).saveUserAsPatient(createUserRequest, multipartFiles);
        verify(addressService, times(1)).save(createAddressRequest);
        verify(patientRepository, times(1)).save(any(Patient.class));
        verify(mailService, times(1)).send(any(), any());
    }


    @Test
    void deletePatientByUserId() {

    }

    @Test
    void findPatientById_Should_Return_Patient() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();
        CreateAddressRequest createAddressRequest = new CreateAddressRequest();
        MultipartFile[] multipartFiles = new MultipartFile[0];
        Locale locale = new Locale("eng");

        Patient patient = patientService.registerPatient(createUserRequest, createPatientRequest, createAddressRequest, multipartFiles, locale);

        given(patientService.findPatientById(patient.getId())).willReturn(patient);

        Patient patientById = patientService.findPatientById(patient.getId());

        assertThat(patient).isEqualTo(patientById);

    }

    @Test
    void findPatientById_Should_Th() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        CreatePatientRequest createPatientRequest = new CreatePatientRequest();
        CreateAddressRequest createAddressRequest = new CreateAddressRequest();
        MultipartFile[] multipartFiles = new MultipartFile[0];
        Locale locale = new Locale("eng");

        patientService.registerPatient(createUserRequest, createPatientRequest, createAddressRequest, multipartFiles, locale);

        given(patientService.findPatientById(1)).willReturn(null);

        Patient patientById = patientService.findPatientById(1);

        assertThat(patientById).isNull();

    }

}