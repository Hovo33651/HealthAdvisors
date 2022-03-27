package com.example.healthadvisors.controller;

import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
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
public class MainController {

    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final RatingService ratingService;
    @Value("${health.advisors.analysis.files.upload.path}")
    private String analysisFilesPath;
    @Value("${health.advisors.specialization.icons.path}")
    private String specIconsPath;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        String userType = currentUser.getUser().getType().name();
        switch (userType) {
            case "PATIENT":
                return "patientHomePage";
            case "DOCTOR":
                map.addAttribute("rating",ratingService.getDoctorRating(currentUser.getUser().getDoctor().getId()));
                return "doctorHomePage";
            default:
                return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "login";
        }else{
            return "redirect:/homePage";
        }
    }

    @GetMapping(value = "/getSpecIconImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getSpecIcon(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(specIconsPath, picName);
    }

    @GetMapping(value = "/getAnalysisFiles", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getAnalysisFiles(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(analysisFilesPath, picName);
    }

}
