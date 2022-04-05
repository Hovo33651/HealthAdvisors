package com.example.healthadvisors.controller;

import com.example.healthadvisors.dto.CreateAddressRequest;
import com.example.healthadvisors.dto.CreatePatientRequest;
import com.example.healthadvisors.dto.CreateUserRequest;
import com.example.healthadvisors.entity.Doctor;
import com.example.healthadvisors.entity.User;
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

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final UserService userService;
    private final FileUploadDownLoadUtils fileUploadDownLoadUtils;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final RatingService ratingService;

    @Value("${health.advisors.patient.pictures.upload.path}")
    String path;




    /**
     * redirects null current user into register page
     */
    @GetMapping("/register")
    public String addUser() {
        return "register";
    }




    /**
     * accepts data from null current user
     * if any field has not filled correctly, throws error
     * creates entity instances from dto
     * saves entities in database
     * sends activation email to not null, not active current user
     */
    @PostMapping("/register")
    public String addUser(@ModelAttribute @Valid CreateUserRequest createUserRequest,
                          BindingResult bindingResult,
                          @ModelAttribute CreatePatientRequest createPatientRequest,
                          @ModelAttribute CreateAddressRequest createAddressRequest,
                          @RequestParam("picture") MultipartFile[] uploadedFiles,
                          ModelMap map,
                          Locale locale) throws IOException, MessagingException {

        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError allError : bindingResult.getAllErrors()) {
                errors.add(allError.getDefaultMessage());
            }
            map.addAttribute("errors", errors);
            return "register";
        }

        patientService.registerUser(createUserRequest, createPatientRequest,
                createAddressRequest, uploadedFiles, map, locale);
        return "redirect:/loginPage";
    }




    /**
     * finds user from database with sent token
     * if null sends a message
     * if active sends a message
     * else sets active true
     * removes user's token
     * removes user's token created date
     * sends message to active user
     */
    @GetMapping("/user/activate")
    public String activateUser(ModelMap map,
                               @RequestParam(name = "token") String token) {

        Optional<User> user = userService.findByToken(token);
        if (!user.isPresent()) {
            map.addAttribute("message", "User does not exists");
            return "activateUser";
        }
        User userFromDb = user.get();
        if (userFromDb.isActive()) {
            map.addAttribute("message", "Your account is activated");
            return "activateUser";
        }
        userFromDb.setActive(true);
        userFromDb.setToken(null);
        userFromDb.setTokenCreatedDate(null);
        userService.saveUser(userFromDb);
        map.addAttribute("message", "Your account has been activated. \n Please sign in");
        return "redirect:/login";
    }




    /**
     * accepts patient's picture url
     * returns picture by byte array
     */
    @GetMapping(value = "/getPatientImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileUploadDownLoadUtils.getImage(path, picName);
    }



    /**
     * shows current user(patient) the list of doctors using pagination
     */
    @GetMapping("/makeAppointment")
    public String chooseDoctor(ModelMap map,
                               @RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "5") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Doctor> allDoctors = doctorService.findAllDoctors(pageRequest);
        map.addAttribute("doctors", allDoctors);

        int totalPages = allDoctors.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            map.addAttribute("pageNumbers", pageNumbers);
        }

        return "viewAllDoctors";
    }



    /**
     * accepts the doctor id
     * accepts the appointment date and time
     * saves the appointment in database
     */
    @PostMapping("/newAppointment")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  @RequestParam("appointmentDate") String appointmentDate,
                                  @AuthenticationPrincipal CurrentUser currentUser) {
        patientService.newAppointment(doctorId, appointmentDate, currentUser);
        return "redirect:/home";
    }



    /**
     * accepts the appointment id
     * removes appointment from database
     */
    @PostMapping("/discardAppointment/{appointmentId}")
    public String discardAppointment(@PathVariable int appointmentId) {
        appointmentService.deleteAppointmentById(appointmentId);
        return "redirect:/home";
    }



    /**
     * accepts the doctor id
     * finds the doctor by id
     * finds doctor's average rating from database
     */
    @GetMapping("/doctor")
    public String makeAppointment(@RequestParam("doctorId") int doctorId,
                                  ModelMap map) {
        map.addAttribute("doctor", doctorService.findDoctorById(doctorId));
        map.addAttribute("rating", ratingService.getDoctorRating(doctorId));
        return "viewDoctorPage";
    }

}