package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Address;
import com.example.healthadvisors.entity.Appointment;
import com.example.healthadvisors.entity.Patient;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final AddressService addressService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;

    private final AppointmentService appointmentService;
    private final DoctorService doctorService;

    @Value("${health.advisors.patient.pictures.upload.path}")
    String path;



    @GetMapping("/register")
    public String addUser() {
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute @Valid CreateUserRequest createUserRequest,
                          BindingResult bindingResult,
                          @ModelAttribute CreatePatientRequest createPatientRequest,
                          @ModelAttribute CreateAddressRequest createAddressRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles,
                          ModelMap map) throws IOException {

        if(bindingResult.hasErrors()){
            List<String> errors = new ArrayList<>();
            for (ObjectError allError : bindingResult.getAllErrors()) {
                errors.add(allError.getDefaultMessage());
            }
            map.addAttribute("errors",errors);
            return "register";
        }

        User newUser = userService.saveUserAsPatient(modelMapper.map(createUserRequest, User.class), uploadedFiles);

        Address newAddress = addressService.save(modelMapper.map(createAddressRequest, Address.class));

        Patient patient = modelMapper.map(createPatientRequest, Patient.class);
        patient.setUser(newUser);
        patient.setAddress(newAddress);
        patientService.save(patient);


        String subject = "WELCOME TO OUR WEBSITE";
        String message = "Dear "+ newUser.getName() +  ", you are successfully registered";

        mailService.sendEmail(newUser.getEmail(),subject,message);

        return "redirect:/loginPage";
    }

    @GetMapping(value = "/getPatientImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path,picName);
    }


    @GetMapping("/newAppointment")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  @AuthenticationPrincipal CurrentUser currentUser){

        Appointment newAppointment = Appointment.builder()
                .doctor(doctorService.findDoctorById(doctorId))
                .patient(patientService.findPatientById(currentUser.getUser().getPatient().getId()))
                .appointmentDate(LocalDateTime.now())
                .build();
        appointmentService.saveAppointment(newAppointment);
        return "redirect:/homePage";
    }


   @PostMapping("/discardAppointment/{appointmentId}")
    public String discardAppointment(@PathVariable int appointmentId){
        appointmentService.deleteAppointmentById(appointmentId);
        return "redirect:/home";
    }

}