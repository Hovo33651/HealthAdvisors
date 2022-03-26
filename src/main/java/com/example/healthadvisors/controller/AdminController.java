package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateDoctorRequest;
import com.example.healthadvisors.dto.CreateSpecializationRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.service.DoctorService;
import com.example.healthadvisors.service.SpecializationService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;


    @GetMapping("/addDoctor")
    public String addPatientPage(ModelMap map) {
        map.addAttribute("specializations",specializationService.findAll());
        return "registerDoctor";
    }



    @PostMapping("/addDoctor")
    public String addUser(@ModelAttribute CreateUserRequest createUserRequest,
                          @ModelAttribute CreateDoctorRequest createDoctorRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles) throws IOException {

        User newUser = userService.saveUserAsDoctor(modelMapper.map(createUserRequest,User.class),uploadedFiles);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);
        doctor.setUser(newUser);

        doctorService.save(doctor);

        return "redirect:/loginPage";
    }


    @GetMapping("/addSpecialization")
    public String addSpecialization(){
        return "addSpecialization";
    }

    @PostMapping("/addSpecialization")
    public String addSpecialization(@ModelAttribute CreateSpecializationRequest createSpecializationRequest,
                                    @RequestParam("icon") MultipartFile[] uploadedFiles) throws IOException {
        specializationService.save(createSpecializationRequest,uploadedFiles);
        return "addSpecialization";
    }


}
