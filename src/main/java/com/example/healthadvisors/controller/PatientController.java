package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.AddressService;
import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/userPage")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("currentUser", currentUser);
        map.addAttribute("medReport", medReportService.findMedReportByPatientId(currentUser.getUser().getId()));
        return "userPage";
    }

    @GetMapping("/addUser")
    public String addPatientPage() {
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute CreateUserRequest createUserRequest,
                          @ModelAttribute CreatePatientRequest createPatientRequest,
                          @ModelAttribute CreateAddressRequest createAddressRequest) {

        User user = modelMapper.map(createUserRequest, User.class);
        Patient patient = modelMapper.map(createPatientRequest, Patient.class);
        Address address = modelMapper.map(createAddressRequest, Address.class);

        User newUser = userService.save(user);
        Address newAddress = addressService.save(address);
        patient.setUser(newUser);
        patient.setAddress(newAddress);
        Patient newPatient = patientService.save(patient);
        newUser.setPatient(newPatient);

        return "redirect:/loginPage";
    }

}