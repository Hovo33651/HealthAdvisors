package com.example.healthadvisors.controller;

import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class DoctorController {

    private final MedReportService medReportService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    @Value("${health.advisors.doctor.pictures.upload.path}")
    String path;


    @GetMapping("/viewPatients/{doctorId}")
    public String viewAllPatients(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @PathVariable int doctorId) {
        PageRequest pageRequest = PageRequest.of(page, size);
        medReportService.findPatientsByDoctorId(doctorId,pageRequest);
        return "allPatientsPage";
    }

    @GetMapping(value = "/getDoctorImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path,picName);
    }

}


