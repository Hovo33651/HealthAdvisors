package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateDoctorRequest;
import com.example.healthadvisors.dto.CreateSpecializationRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.DoctorService;
import com.example.healthadvisors.service.SpecializationService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
@Slf4j
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DoctorService doctorService;
    private final SpecializationService specializationService;


    /**
     * redirects the page to registerDoctor.html
     */
    @GetMapping("/addDoctor")
    public String addPatientPage(ModelMap map) {
        map.addAttribute("specializations", specializationService.findAll());
        return "registerDoctor";
    }


    /**
     * controller works only for ADMIN
     * accepts User dto data
     * accepts Doctor dto data
     * accepts User's picture
     * creates instances by ModelMapper
     * saves in database
     */
    @PostMapping("/addDoctor")
    public String addUser(@AuthenticationPrincipal CurrentUser currentUser,
                          @ModelAttribute CreateUserRequest createUserRequest,
                          @ModelAttribute CreateDoctorRequest createDoctorRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles) throws IOException {
        log.info("Admin: {} wants to add a new doctor", currentUser.getUser().getEmail());
        User newUser = userService.saveUserAsDoctor(modelMapper.map(createUserRequest, User.class), uploadedFiles);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);
        doctor.setUser(newUser);
        doctorService.save(doctor);
        log.info("New doctor has been registered. Email: {}", newUser.getEmail());
        return "redirect:/login";
    }


    /**
     * redirects the page to addSpecialization page(for ADMIN only)
     */
    @GetMapping("/addSpecialization")
    public String addSpecialization() {
        return "addSpecialization";
    }


    /**
     * accepts Specialization dto data
     * accepts icon for Specialization
     * saves in database by making an instance by ModelMapper
     */
    @PostMapping("/addSpecialization")
    public String addSpecialization(@AuthenticationPrincipal CurrentUser currentUser,
                                    @ModelAttribute CreateSpecializationRequest createSpecializationRequest,
                                    @RequestParam("icon") MultipartFile[] uploadedFiles) throws IOException {
        log.info("Admin: {} wants to add a new specialization: {}",
                currentUser.getUser().getEmail(),createSpecializationRequest.getName());
        specializationService.save(createSpecializationRequest, uploadedFiles);
        return "addSpecialization";
    }


}
