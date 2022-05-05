package com.example.healthadvisors.controller;

import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.AppointmentService;
import com.example.healthadvisors.service.MedReportService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final MedReportService medReportService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    @Value("${health.advisors.doctor.pictures.upload.path}")
    String path;


    /**
     * accepts the Principal
     * accepts pagination
     * sends patient pagination list to viewAllPatients.html
     */
    @GetMapping("/viewAllPatients")
    public String viewAllPatients(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "size", defaultValue = "5") int size,
                                  @AuthenticationPrincipal CurrentUser currentUser,
                                  ModelMap map) {
        log.info("viewAllPatients: request from {} to see all patients", currentUser.getUser().getEmail());
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Patient> patientsByDoctorId = medReportService.findPatientsByDoctorId(currentUser.getUser().getDoctor().getId(), pageRequest);
        map.addAttribute("patients", patientsByDoctorId);

        int totalPages = patientsByDoctorId.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
        }
        log.info("viewAllPatients: response to {} to see all patients", currentUser.getUser().getEmail());
        return "viewAllPatients";
    }
    /**
     * accepts doctor picture name
     * sends picture in byte[]
     */
    @GetMapping(value = "/getDoctorImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path, picName);
    }

    /**
     * accepts patient id
     * finds the patient from database
     * sends patient instance to patientViewPage.html
     */
    @GetMapping("/patient{id}")
    public String singlePatient(@PathVariable int id, ModelMap map) {
        map.addAttribute("patient", patientService.findPatientById(id));
        return "patientViewPage";
    }

    /**
     * finds doctor's active appointments
     * redirects to viewNewAppointments.html
     */
    @GetMapping("/appointments")
    public String getActiveAppointments(@AuthenticationPrincipal CurrentUser currentUser,
                                        ModelMap map) {
        log.info("getActiveAppointments: request from {} to see active appointments", currentUser.getUser().getEmail());
        map.addAttribute("appointments", appointmentService.
                findActiveAppointmentsByDoctorId(currentUser.getUser().getDoctor().getId()));
        log.info("getActiveAppointments: response to {} to see active appointments", currentUser.getUser().getEmail());
        return "viewNewAppointments";
    }

    /**
     * accepts appointment id
     * accepts patient id
     * sets appointment active false
     * finds patient by id, adds as attribute in ModelMap
     * redirects to createMedReport.html
     */
    @GetMapping("/medReport")
    public String createMedReport(@AuthenticationPrincipal CurrentUser currentUser,
                                  @RequestParam("appointmentId") int appointmentId,
                                  @RequestParam("patientId") int patientId,
                                  ModelMap map) {
        Patient patient = patientService.findPatientById(patientId);
        log.info("createMedReport: request from {} to create a new medical report for the patient {}",
                currentUser.getUser().getEmail(),patient.getUser().getEmail());
        appointmentService.setAppointmentActiveFalse(appointmentId);
        map.addAttribute("patient", patient);
        return "createMedReport";
    }
}


