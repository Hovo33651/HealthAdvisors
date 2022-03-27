package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.*;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.*;
import com.example.healthadvisors.util.FileUploadDownLoadUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    private final RatingService ratingService;

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

    @GetMapping("/makeAppointment")
    public String chooseDoctor(ModelMap map,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size){
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Doctor> allDoctors = doctorService.findAllDoctors(pageRequest);
        map.addAttribute("doctors",allDoctors);

        int totalPages = allDoctors.getTotalPages();
        if(totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed(0,totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers",pageNumbers);
        }

        return "viewAllDoctors";
    }

    @GetMapping("/doctor")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  ModelMap map){
        map.addAttribute("doctor",doctorService.findDoctorById(doctorId));
        map.addAttribute("rating",ratingService.getDoctorRating(doctorId));
        return "viewDoctorPage";
    }

    @PostMapping("/newAppointment")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  @RequestParam("appointmentDate") String appointmentDate,
                                  @AuthenticationPrincipal CurrentUser currentUser){

        Doctor doctor = doctorService.findDoctorById(doctorId);
        Patient patient = currentUser.getUser().getPatient();

        Appointment newAppointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(LocalDateTime.parse(appointmentDate))
                .build();

        newAppointment.setPatient(patient);
        newAppointment.setDoctor(doctor);

        appointmentService.saveAppointment(newAppointment);

        String subject = "New Appointment";
        String message = "Doctor " +  doctor.getUser().getSurname() + ", you have a new appointment. \n " +
                "Patient: " + patient.getUser().getName() + " " + patient.getUser().getSurname() + "\n" +
                "Date " + newAppointment.getAppointmentDate();

        mailService.sendEmail(doctor.getUser().getEmail(),subject,message);

        return "redirect:/home";
    }


   @PostMapping("/discardAppointment/{appointmentId}")
    public String discardAppointment(@PathVariable int appointmentId){
        appointmentService.deleteAppointmentById(appointmentId);
        return "redirect:/home";
    }

}