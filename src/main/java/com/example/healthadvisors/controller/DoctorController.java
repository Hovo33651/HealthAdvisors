package com.example.healthadvisors.controller;


import com.example.healthadvisors.dto.CreateDoctorRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.User;
import com.example.healthadvisors.security.CurrentUser;
import com.example.healthadvisors.service.DoctorService;
import com.example.healthadvisors.service.PatientService;
import com.example.healthadvisors.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class DoctorController {

    private final DoctorService doctorService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PatientService patientService;


    @GetMapping("/loginPage")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/doctorPage")
    public String login(@AuthenticationPrincipal CurrentUser currentUser, ModelMap map) {
        map.addAttribute("currentUser", currentUser);

        return "doctorPage";
    }


    @GetMapping("doctor/add")
    public String addDoctorPage() {
        return "doctorRegister";
    }


    @PostMapping("/doctor/register")
    public String addDoctor(@ModelAttribute CreateUserRequest createUserRequest,
                            @ModelAttribute CreateDoctorRequest createDoctorRequest) {

        User user = modelMapper.map(createUserRequest, User.class);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);

        User newUser = userService.save(user);
        Doctor newDoctor = doctorService.save(doctor);
        newUser.setDoctor(newDoctor);

        return "redirect:/loginPage";

    }

    @GetMapping("/doctor/viewPatients")
    public String PatientsOfDoctor(@ModelAttribute CreateUserRequest createUserRequest,
                                   @ModelAttribute CreatePatientRequest createPatientRequest,
                                   @ModelAttribute CreateDoctorRequest createDoctorRequest,
                                   @RequestParam (value = "page",defaultValue = "0") int page,
                                   @RequestParam (value = "size",defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Doctor doctor = modelMapper.map(createDoctorRequest, Doctor.class);
        patientService.findPatientsByDoctorId(doctor.getId(),pageRequest);
        return "/allPatientsPage";
    }
}
