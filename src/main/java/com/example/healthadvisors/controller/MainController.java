package com.example.healthadvisors.controller;

import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MedReportService medReportService;
    private final TestimonialService testimonialService;
    private final CertificateService certificateService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Value("${health.advisors.upload.path}")
    private String imagePath;


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
                return "404";
            case "DOCTOR":
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


    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(imagePath + picName));
        return IOUtils.readAllBytes(inputStream);
    }
}
