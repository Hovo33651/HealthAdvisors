package com.example.healthadvisors.controller;

import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.AddressService;
import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.service.PatientService;
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

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/loginPage")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("currentUser", currentUser);
        map.addAttribute("medReport", medReportService.findMedReportByPatientId(currentUser.getUser().getId()));
        return "userPage";
    }

    @GetMapping("/addUser")
    public String addPatientPage() {

        return "addPatient";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute Patient patient, Address address, ModelMap map) {
        map.addAttribute("address", addressService.save(address));
        map.addAttribute("patient", patient);
        patientService.save(patient);
        return "patientPage";
    }
}
