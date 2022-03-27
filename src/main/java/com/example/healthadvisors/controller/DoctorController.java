package com.example.healthadvisors.controller;

import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private final MedReportService medReportService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final PatientService patientService;
    @Value("${health.advisors.doctor.pictures.upload.path}")
    String path;


    @GetMapping("/viewAllPatients")
    public String viewAllPatients(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @AuthenticationPrincipal CurrentUser currentUser,
                                  ModelMap map) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patientsByDoctorId = medReportService.findPatientsByDoctorId(currentUser.getUser().getDoctor().getId(), pageRequest);
        map.addAttribute("patients",patientsByDoctorId);

        int totalPages = patientsByDoctorId.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(0,totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers",pageNumbers);
        }
        return "viewAllPatients";
    }

    @GetMapping(value = "/getDoctorImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path,picName);
    }

    @GetMapping("/patient{id}")
    public String singlePatient(@PathVariable int id, ModelMap map){
        map.addAttribute("patient",patientService.findPatientById(id));
        return "patientViewPage";
    }

}


