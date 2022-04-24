package com.example.healthadvisors.controller;

import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final RatingService ratingService;
    private final TestimonialService testimonialService;
    private final AppointmentService appointmentService;

    @Value("${health.advisors.analysis.files.upload.path}")
    private String analysisFilesPath;
    @Value("${health.advisors.specialization.icons.path}")
    private String specIconsPath;


    /**
     * redirects the open site request to index.html
     */
    @GetMapping("/")
    public String index(ModelMap map) {
        map.addAttribute("testimonials", testimonialService.getAllTestimonials());
        return "index";
    }

    /**
     * accepts the principal
     * checks principal type
     * depending on type redirects the request to principal home page
     * if principal is not a DOCTOR or PATIENT, then request is redirected to main page
     */
    @GetMapping("/home")
    public String redirectToHomePage(@AuthenticationPrincipal CurrentUser currentUser) {
       log.info("Request from {} to redirect to home page",currentUser.getUser().getEmail());
        String userType = currentUser.getUser().getType().name();
        switch (userType) {
            case "PATIENT":
                log.info("Patient {} redirected to home page",currentUser.getUser().getEmail());
                return "patientHomePage";
            case "DOCTOR":
                log.info("Doctor {} redirected to home page",currentUser.getUser().getEmail());
                currentUser.getUser().getDoctor().setRating(ratingService.getDoctorRating(currentUser.getUser().getDoctor().getId()));
                return "doctorHomePage";
            default:
                log.info("Principal {} redirected to main page",currentUser.getUser().getEmail());
                return "redirect:/";
        }
    }

    /**
     * accepts the principal
     * redirects to /home
     */
    @GetMapping("/login")
    public String login(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "login";
        } else {
            return "redirect:/home";
        }
    }

    /**
     * accepts specialization picture name
     * sends picture in byte[]
     */

    @GetMapping(value = "/getSpecIconImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getSpecIcon(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(specIconsPath, picName);
    }


    /**
     * accepts analysis file name
     * sends picture in byte[]
     */

    @GetMapping(value = "/getAnalysisFiles", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getAnalysisFiles(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(analysisFilesPath, picName);
    }

}
