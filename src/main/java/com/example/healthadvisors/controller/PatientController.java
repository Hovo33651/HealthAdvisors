package com.example.healthadvisors.controller;

import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AddressService addressService;
    private final MedReportService medReportService;

    private final UserService userService;
    private final DoctorService doctorService;

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/userPage")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("currentUser", currentUser);
        map.addAttribute("medReport", medReportService.findMedReportByPatientId(currentUser.getUser().getId()));
        return "patientPage";
    }

    @GetMapping("/addUser")
    public String addPatientPage() {
        return "register";
    }


    @PostMapping("/register")
    public String addUser(ModelMap map,
                          @ModelAttribute User user,
                          @ModelAttribute Patient patient,
                          @ModelAttribute Address address,
                          @AuthenticationPrincipal CurrentUser currentUser) {
        User newUser = userService.save(user);
        Address newAddress = addressService.save(address);
        patient.setUser(newUser);
        patient.setAddress(newAddress);
        map.addAttribute("user", user);
        patientService.save(patient);
        if (currentUser == null) {
            currentUser = new CurrentUser(newUser);
            map.addAttribute("currentUser", currentUser);
        }
        return "patientPage";
    }


}
