package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateDoctorRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.MedReport;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.service.DoctorService;
import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DoctorController {
    private final UserService userService;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final ModelMapper modelMapper;
    private final MedReportService medReportService;




    @GetMapping("/addDoctor")
    public String addPatientPage() {
        return "registerDoctor";
    }

    @PostMapping("/addDoctor")
    public String addUser(@ModelAttribute CreateUserRequest createUserRequest,
                          @ModelAttribute CreateDoctorRequest createDoctorRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles) throws IOException {

        User newUser = userService.saveDoctor(modelMapper.map(createUserRequest,User.class),uploadedFiles);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);
        doctor.setUser(newUser);

        doctorService.save(doctor);

        return "redirect:/loginPage";
    }

    @GetMapping("/viewPatients/{doctorId}")
    public String viewAllPatients(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @PathVariable int doctorId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        medReportService.findPatientsByDoctorId(doctorId,pageRequest);
        return "allPatientsPage";
    }


}


