package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateDoctorRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.DoctorService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DoctorController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ModelMapper modelMapper;




    @GetMapping("/addDoctor")
    public String addPatientPage() {
        return "registerDoctor";
    }

    @PostMapping("/addDoctor")
    public String addUser(@ModelAttribute CreateUserRequest createUserRequest,
                          @ModelAttribute CreateDoctorRequest createDoctorRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles) throws IOException {

        User user = modelMapper.map(createUserRequest, User.class);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);

        User newUser = userService.save(user,uploadedFiles);
        Doctor newDoctor = doctorService.save(doctor);
        newUser.setDoctor(newDoctor);

        return "redirect:/loginPage";
    }

    @GetMapping("/viewPatients")
    public String viewAllPatients(@ModelAttribute CreatePatientRequest createPatientRequest,
                                  @ModelAttribute CreateDoctorRequest createDoctorRequest,
                                  @ModelAttribute CreateUserRequest createUserRequest,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);
//        patientService.findPatientsByDoctorId(doctor.getId(), pageRequest);
        return "allPatientsPage";
    }


}


