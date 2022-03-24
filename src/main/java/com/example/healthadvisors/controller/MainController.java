package com.example.healthadvisors.controller;

import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MedReportService medReportService;
    private final TestimonialService testimonialService;
    private final CertificateService certificateService;
    private final DoctorService doctorService;
    private final PatientService patientService;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/homePage")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("currentUser", currentUser);
        map.addAttribute("medReport", medReportService.findMedReportByPatientId(currentUser.getUser().getId()));
        String userType = currentUser.getUser().getType().name();
        switch (userType) {
            case "ADMIN":
                return "redirect:/";
            case "PATIENT":
                map.addAttribute("testimonials", testimonialService.findTestimonialsByUserId(currentUser.getUser().getId()));
                map.addAttribute("userDoctors", doctorService.findDoctorsByMedReports(medReportService.findMedReportByPatientId(currentUser.getUser().getId())));
                return "patientPage";
            case "DOCTOR":
                map.addAttribute("patients", patientService.findPatientsByMedReports(medReportService.findMedReportByDoctorUserId(currentUser.getUser().getId())));
                map.addAttribute("certificates", certificateService.findCertificatesByDoctorUserId(currentUser.getUser().getId()));
                map.addAttribute("doctor", doctorService.findDoctorByUserId(currentUser.getUser().getId()));
                return "doctorPage";
            default:
                return "/loginPage";
        }
    }

    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }
}
